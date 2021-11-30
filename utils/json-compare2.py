import json
import os
import argparse
import re
import time
import logging
import sys
from collections import defaultdict
from itertools import zip_longest
from datetime import datetime

'''
usage: json-compare2.py [-h] [-f FOLDER] -f1 FILE1 -f2 FILE2 [-l LOG] [-o]

Check if 2 json files are the same (regardless of key or list order), and
optionally write the ordered json files with the suffix "_cmp".

optional arguments:
  -h, --help            show this help message and exit
  -f FOLDER, --Folder FOLDER
                        Root folder.
  -f1 FILE1, --File1 FILE1
                        First file to be compared.
  -f2 FILE2, --File2 FILE2
                        Second file to be compared.
  -l LOG, --Log LOG     Path to log file. Defaults to root.
  -o, --Output          Flag indicating wether to write output files or not.

examples:
python json-compare2.py -f1 "reports/2.4.7/annotationtest.json" -f2 "reports/2.4.8.1/annotationtest.json" -o -l "reports/logs"
python json-compare2.py -f1 "reports/2.4.7/annotationtest.json" -f2 "reports/2.4.8.2/annotationtest.json" -o -l "reports/logs"
python json-compare2.py -f1 "reports/2.4.8.1/annotationtest.json" -f2 "reports/2.4.8.2/annotationtest.json" -o -l "reports/logs"

python json-compare2.py -f1 "reports/2.4.7/spring-boot-2.6.0-M3.json" -f2 "reports/2.4.8.1/spring-boot-2.6.0-M3.json" -o -l "reports/logs"
python json-compare2.py -f1 "reports/2.4.7/spring-boot-2.6.0-M3.json" -f2 "reports/2.4.8.2/spring-boot-2.6.0-M3.json" -o -l "reports/logs"
python json-compare2.py -f1 "reports/2.4.8.1/spring-boot-2.6.0-M3.json" -f2 "reports/2.4.8.2/spring-boot-2.6.0-M3.json" -o -l "reports/logs"

python json-compare2.py -f1 "reports/2.4.7/spring-boot-2.6.0-M3.json" -f2 "reports/2.4.7/spring-boot-project-CV.json" -o -l "reports/logs"
python json-compare2.py -f1 "reports/2.4.7/spring-boot-2.6.0-M3.json" -f2 "reports/2.4.7/spring-boot-project-PV.json" -o -l "reports/logs"
python json-compare2.py -f1 "reports/2.4.7/spring-boot-2.6.0-M3.json" -f2 "reports/2.4.7/spring-boot-project-SV.json" -o -l "reports/logs"

'''
description = '''
    Check if 2 json files are the same (regardless of key or list order), and
    optionally write the ordered json files with the suffix "_cmp".
    '''

def main():
    # Initialize parser
    parser = argparse.ArgumentParser(description=description)
    parser.add_argument("-f", "--Folder", help = "Root folder.", default="")
    parser.add_argument("-f1", "--File1", help = "First file to be compared.", required=True)
    parser.add_argument("-f2", "--File2", help = "Second file to be compared.", required=True)
    parser.add_argument("-l", "--Log", help = "Path to log file. Defaults to root.", default="")
    parser.add_argument("-o", "--Output", action="store_true", \
        help= "Flag indicating wether to write output files or not.")
    
    # Read arguments from command line
    args = parser.parse_args()
    path1 = os.path.join(args.Folder, args.File1)
    path2 = os.path.join(args.Folder, args.File2)
    write = args.Output
    log_folder = args.Log

    # Initialize logger
    #log_file = f"{abs(hash(path1+path2))}.log"
    log_file = datetime.today().strftime('%Y_%m_%d-%H_%M_%S.log')
    log_file = os.path.join(log_folder,log_file)
    logger = makeLogger(log_file)
    logger.info(f'Logging to {log_file}')

    # Reindentation
    logger.info('Reindentation...')
    path_out1 = path1[:-5]+"_cmp.json"
    path_out2 = path2[:-5]+"_cmp.json"
    logger.info(f"Writting output JSON files: {path_out1} {path_out2}")
    reindent(path1, path_out1)
    reindent(path2, path_out2)
    
    # Size comparison
    logger.info('Size comparison...')
    s1 = os.path.getsize(path_out1)
    s2 = os.path.getsize(path_out2)
    logger.info(f"JSON sizes match? {s1 == s2} {s1} {s2}")

    # Line count comparison
    logger.info('Line count comparison...')
    l1 = getFileLineCount(path_out1)
    l2 = getFileLineCount(path_out2)
    logger.info(f"JSON line count match? {l1 == l2} {l1} {l2}")

    # Sorting jsons
    logger.info('Sorting jsons...')
    logger.info(f"Writting output JSON files: {path_out1} {path_out2}")
    _, i1 = sortJson(path_out1, path_out1, n_max=100)
    _, i2 = sortJson(path_out2, path_out2, n_max=100)
    logger.info(f"JSON's # reorders: {i1} {i2}")

    # Contents comparison
    logger.info("Contents comparison...")
    json1 = getFileString(path_out1)
    json2 = getFileString(path_out2)
    logger.info(f"JSON contents match? {json1==json2}")
    if not json1==json2:
        key_diffs, n = compareJsons(path_out1, path_out2)
        logger.info(f"Number of different keys: {n}")
    
    # Ordered lines comparison
    logger.info("Ordered lines comparison...")
    lines1 = getFileOrderedLines(path_out1)
    lines2 = getFileOrderedLines(path_out2)

    if write:
        logger.info(f"Writting output TXT files: {path_out1[:-5]}.txt {path_out2[:-5]}.txt")
        writeLines(path_out1[:-5]+".txt", lines1)
        writeLines(path_out2[:-5]+".txt", lines2)

    logger.info(f"JSON ordered lines match? {lines1 == lines2}")
    if not lines1 == lines2:
        equal_lines = compareLines(lines1, lines2, l1, l2)
        logger.info(f"Number of equal ordered lines:{equal_lines}/{max(l1,l2)}")

    # Cleanup
    if not write:
        logger.info(f"Removing output JSON files: {path_out1} {path_out2}")
        try:
            os.remove(path_out1)
            os.remove(path_out2)
        except:
            pass

    # Log dict
    if not json1==json2:
        key_diffs_file = log_file[:-4]+'.json'
        key_diffs = {k:v for k,v in key_diffs.items() if v[0]>0}
        key_diffs = {k:v for k,v in sorted(key_diffs.items(), key=lambda kv: kv[1][0])}
        logger.info(f"Writting output key difference report: {key_diffs_file}")
        with open(key_diffs_file, 'w') as f:
            json.dump(key_diffs, f, indent=2)
        
        logger.info(f"Key difference contents:")
        key_diffs_lines = [f" - {k}: {v[0]}/{v[1]}" for k,v in key_diffs.items()]
        for line in key_diffs_lines:
            logger.info(line)

def makeLogger(filename):
    logger = logging.getLogger('')
    logger.setLevel(logging.DEBUG)
    fh = logging.FileHandler(filename)
    sh = logging.StreamHandler(sys.stdout)
    formatter = logging.Formatter('[%(asctime)s] %(levelname)s [%(filename)s.%(funcName)s:%(lineno)d] %(message)s', datefmt='%a, %d %b %Y %H:%M:%S')
    fh.setFormatter(formatter)
    sh.setFormatter(formatter)
    logger.addHandler(fh)
    logger.addHandler(sh)
    return logger

def reindent(path, path_out):
    data = None
    with open(path) as f: 
        data = json.load(f)
    time.sleep(1)
    with open(path_out, 'w') as f:
        json.dump(data, f, indent=2)

def sortedDeep(d):
    # source: https://www.titanwolf.org/Network/q/e7f3720e-1c8d-4da7-b925-cd2a4301192a/y
    #def makeTuple(v): return (*v,) if isinstance(v,(list,dict)) else (v,)
    if isinstance(d,list):
        return sorted( map(sortedDeep,d), key=json.dumps )
    if isinstance(d,dict):
        return { k: sortedDeep(d[k]) for k in sorted(d)}
    return d

def applyNtimes(d,f,n):
    for _ in range(n):
        d = f(d)
    return d

def makeHash(d):
    check = ''
    for key in d:
        check += str(d[key])
    return hash(check)

def applyUntilFinal(d,f,n=10000):
    checksum_ = 0
    checksum = makeHash(d)
    i=0
    while checksum != checksum_ and i<n:
        checksum_ = checksum
        d = f(d)
        checksum = makeHash(d)
        i+=1
    return d, i

def sortJson(path, path_out, n_max=100):
    data = None
    with open(path) as f: 
        data = json.load(f)
    data, i = applyUntilFinal(data, sortedDeep, n_max)
    with open(path_out, 'w') as f:
        json.dump(data, f, indent=2, sort_keys=True)
    return data, i

def getFileLineCount(path): 
    return sum(1 for line in open(path))

def getFileString(path):
    data = None
    with open(path) as f: 
        data = json.load(f)
    return json.dumps(data, sort_keys=True)

def getFileOrderedLines(path):
    lines = None
    with open(path) as f: lines = f.readlines()
    return sorted(lines)

def writeLines(path, lines):
    with open(path, 'w') as f:
        for line in lines:
            f.write(line)

def compareLines(lines1, lines2, l1, l2):
    if l2>=l1:
        lines1 = lines1+["" for _ in range(l2-l1)]
    else:
        lines2 = lines2+["" for _ in range(l1-l2)]
    return sum([a==b for a,b in zip(lines1,lines2)])

def compareDeep(d1,d2):
    global key_diffs
    N = 1
    if json.dumps(d1) == json.dumps(d2):
        return 0
    if isinstance(d1,list) and isinstance(d2,list):
        for k1, k2 in zip_longest(d1,d2, fillvalue='_'):
            n = compareDeep(k1,k2)
            #N += n
    if isinstance(d1,dict) and isinstance(d2,dict):
        for k1, k2 in zip_longest(d1,d2, fillvalue='_'):
            try:    d11 = d1[k1]
            except: d11 = '_'
            try:    d22 = d2[k2]
            except: d22 = '_'
            n = compareDeep(d11,d22)
            key_diffs[k1][0] += n
            key_diffs[k1][1] += 1
            key_diffs[k2][0] += n
            key_diffs[k2][1] += 1
            #N += n
    return N

def compareJsons(path1, path2):
    data1 = data2 = None
    global key_diffs
    key_diffs = defaultdict(lambda: [0,0])
    with open(path1) as f: data1 = json.load(f)
    with open(path2) as f: data2 = json.load(f)
    n = compareDeep(data1,data2)
    return key_diffs, n

if __name__=="__main__":
    main()
import json
import os
import argparse
from datetime import datetime
import logging
import sys
import glob
import re
from collections import defaultdict

'''
usage: annotation-schema-hunter.py [-h] [-f FOLDER] [-o OUTPUT]

optional arguments:
  -h, --help            show this help message and exit
  -f FOLDER, --Folder FOLDER
                        Root folder. Defaults to root.
  -o OUTPUT, --Output OUTPUT
                        Path to output json and log. Defaults to root.
examples:
python annotation-schema-hunter.py -f "C:/asniffer/spring-framework-main" -o "schemas"
'''

def main():
    # Start time
    start = datetime.now()

    # Initialize parser
    parser = argparse.ArgumentParser(description="")
    parser.add_argument("-f", "--Folder", help = "Root folder. Defaults to root.", default="")
    parser.add_argument("-o", "--Output", help = "Path to output json and log. Defaults to root.", default="")
    
    # Read arguments from command line
    args = parser.parse_args()
    path_root = args.Folder
    if(not os.path.exists(path_root)): 
        raise Exception("Path does not exists.")

    date = datetime.today().strftime('%Y_%m_%d-%H_%M_%S-')
    date_plus_name = date+os.path.basename(os.path.normpath(path_root))
    file_log = os.path.join(args.Output, date_plus_name+'.log')
    file_json = os.path.join(args.Output, date_plus_name+'.json')
    file_aux = os.path.join(args.Output, date_plus_name+'_aux'+'.txt')

    # Init logger
    logger = makeLogger(file_log)
    logger.info(f'Logging to {file_log}')

    # Listing files
    logger.info(f'Listing files in {path_root} ...')
    path_wildcard = os.path.join(path_root, "**/*.java")
    files = list(glob.glob(path_wildcard, recursive=True))
    logger.info(f'{len(files)} files found.')

    # Regex
    logger.info(f'Parsing files in {path_root} ...')
    glossary = defaultdict(set)
    schemas = list()
    annotations = list()
    for file_name in files:
        with open(file_name, encoding='utf-8', mode="r") as f:
            contents = f.read()
            match = re.search("@interface\s+[a-zA-Z][a-zA-Z0-9_]*", contents)
            if match:
                logger.info(f"--Match in {file_name}")
                key = re.search("package\s+([a-zA-Z.]*)", contents).group(1)
                schemas.append((file_name, key))
                logger.info(f"----schema: {key}")
                values = re.findall("@interface\s+([a-zA-Z][a-zA-Z0-9_]*)", contents)
                for value in values:
                    logger.info(f"----annotation: {value}")
                    annotations.append((key, value))
                    glossary[key].add(value)
    
    def dict_set2dict_list(d): return {k:list(v) for k,v in d.items()}
    glossary = dict_set2dict_list(glossary)

    def flatten(l): return [item for sublist in l for item in sublist]
    uniqueAnnotations = len(flatten([v for v in glossary.values()]))
    
    logger.info(f'Found {len(schemas)} files containing annotations.')
    logger.info(f'Found {len(glossary)}/{len(schemas)} unique schemas.')
    logger.info(f'Found {uniqueAnnotations}/{len(annotations)} unique annotations.')

    # Output
    logger.info(f'Writing output json to {file_json} ...')
    with open(file_json, 'w') as f:
        json.dump(glossary, f, indent=4)
    logger.info(f'Writing output txt to {file_json} ...')
    with open(file_aux, 'w') as f:
        f.write("ANNOTATIONS\n")
        for line in annotations:
            f.write(str(line)+'\n')
        f.write("SCHEMAS\n")
        for line in schemas:
            f.write(str(line)+'\n')
    
    logger.info(f'Finished in {datetime.now()-start}.')

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

if __name__=="__main__":
    main()
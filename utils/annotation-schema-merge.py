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
usage: annotation-schema-merge.py [-h] [-f FOLDER] [-o OUTPUT]

optional arguments:
  -h, --help            show this help message and exit
  -f FOLDER, --Folder FOLDER
                        Folder containing only json glossary files. Defaults to root.
  -o OUTPUT, --Output OUTPUT
                        Path to output json and log. Defaults to root.
examples:
python annotation-schema-merge.py -f merge -o merged
'''

def main():
    # Start time
    start = datetime.now()

    # Initialize parser
    parser = argparse.ArgumentParser(description="")
    parser.add_argument("-f", "--Folder", help = "Folder containing only json glossary files. Defaults to root.", default="")
    parser.add_argument("-o", "--Output", help = "Path to output json and log. Defaults to root.", default="")
    
    # Read arguments from command line
    args = parser.parse_args()
    path_root = args.Folder
    if(not os.path.exists(path_root)): 
        raise Exception("Input folder does not exists.")

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
    path_wildcard = os.path.join(path_root, "**/*.json")
    files = list(glob.glob(path_wildcard, recursive=True))
    logger.info(f'{len(files)} files found.')

    # Regex
    logger.info(f'Parsing files in {path_root} ...')
    glossary = defaultdict(set)
    glossary_count = defaultdict(list)
    schemas_counter = 0
    for file_name in files:
        g = {}
        with open(file_name, encoding='utf-8', mode="r") as f:
            g = json.load(f)
            logger.info(f"--Json {file_name}")

            uniqueAnnotations = len(flatten([v for v in g.values()]))

            logger.info(f'----Unique schemas: {len(g)}')
            logger.info(f'----Unique annotations: {uniqueAnnotations}')

            g_ = dict_list2dict_set(g)
            for k,s in g_.items():
                glossary[k].update(s)
            for k,l in g.items():
                schemas_counter+=1
                glossary_count[k].extend(l)
    
    glossary = dict_set2dict_list(glossary)
    uniqueAnnotations = len(flatten([v for v in glossary.values()]))
    totalAnnotations = len(flatten([v for v in glossary_count.values()]))

    logger.info(f'Total schemas: {len(glossary)}/{schemas_counter} unique')
    logger.info(f'Total annotations: {uniqueAnnotations}/{totalAnnotations} unique')

    # Output
    logger.info(f'Writing output json to {file_json} ...')
    with open(file_json, 'w') as f:
        json.dump(glossary, f, indent=4)
    
    logger.info(f'Finished in {datetime.now()-start}.')

def flatten(l): return [item for sublist in l for item in sublist]

def dict_set2dict_list(d): return {k:list(v) for k,v in d.items()}
def dict_list2dict_set(d): return {k:set(v) for k,v in d.items()}

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
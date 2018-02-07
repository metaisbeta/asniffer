import os
print("Maven dependencies")
with open("ignoreJarDependencies.txt","w") as f:
    
    for root,dirs,files in os.walk("../../../target/lib"):
        for filename in files:
            f.write("\"-jar:"+filename+"\",\n")
            print(filename)
f.close()    
    
#!/bin/bash
ssh root@127.0.0.1 -p 2222 'rm -rf /tmp/part-r-00000 '
ssh root@127.0.0.1 -p 2222 'hadoop fs -copyToLocal /user/hue/output/part-r-00000 /tmp/'
ssh root@127.0.0.1 -p 2222 'rm -rf /tmp/input.txt'
ssh root@127.0.0.1 -p 2222 'mv /tmp/part-r-00000 /tmp/input.txt'
scp -P 2222 root@127.0.0.1:/tmp/input.txt .
`xattr -c input.txt`
./script.sh input.txt > op.json
mongo xpert_in_development --eval "db.analytics.remove()"
mongoimport -d xpert_in_development -c analytics --jsonArray < op.json

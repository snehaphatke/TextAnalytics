#!/bin/bash
mongoexport -d xpert_in_development -c comments -f _id,body,post_id,user_id,likes -o comment.json
sed -i.bak -e s/'{ \"$oid\" : '/''/g -e s/'},'/,/g  comment.json
rm -rf comment.json.bak
mongoexport -d xpert_in_development -c users -f _id,points,no_of_exp_solutions -o user.json
scp -P 2222 comment.json root@127.0.0.1:/tmp/.
scp -P 2222 user.json root@127.0.0.1:/tmp/.
ssh root@127.0.0.1 -p 2222 'hadoop fs -rm -f /user/hue/comment.json'
ssh root@127.0.0.1 -p 2222 'hadoop fs -rm -f /user/hue/user.json'
ssh root@127.0.0.1 -p 2222 'hadoop fs -rm -r /user/hue/tmpOut'
ssh root@127.0.0.1 -p 2222 'hadoop fs -rm -r /user/hue/output'
ssh root@127.0.0.1 -p 2222 'hadoop fs -copyFromLocal /tmp/comment.json /user/hue/.'
ssh root@127.0.0.1 -p 2222 'hadoop fs -chmod 755 /user/hue/comment.json'
ssh root@127.0.0.1 -p 2222 'hadoop fs -copyFromLocal /tmp/user.json /user/hue/.'
ssh root@127.0.0.1 -p 2222 'hadoop fs -chmod 755 /user/hue/user.json'

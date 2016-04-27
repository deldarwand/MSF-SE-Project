if [ -z ${CSUSER+x} ]; then
	  printf "\nEnter CS username: "
	  read CSUSER
	fi

echo "Syncing server... "
rsync -a --chmod=Du=rwx,Dgo=rx,Fu=rw,Fgo=r ./* $CSUSER@newgate.cs.ucl.ac.uk:/cs/student/www/2015/group2/
echo "done."
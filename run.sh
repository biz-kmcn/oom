sudo rm -fr /tmp/motion/*.jpg
sudo service mothion start
mvn compile exec:java -DsmtpPasswd=temporaryPassword
sudo service mothion stop

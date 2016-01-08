sudo service motion stop
sudo rm -fr /tmp/motion/*.jpg
sudo service motion start
mvn compile exec:java -DsmtpPasswd=temporaryPassword
sudo service mothion stop

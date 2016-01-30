cd "$(dirname "$0")"
javac l.java
java l
rm l.class
rm l\$a.class
killall Terminal
cd "$(dirname "$0")"
unzip p.zip
javac l.java
java l
zip p.zip l.java
rm l.java
rm l.class
rm l\$a.class
rm l.ctxt
rm __MACOSX
killall Terminal
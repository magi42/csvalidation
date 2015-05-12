all: copy-src-to-classpath

copy-src-to-classpath:
	tar c -C src --exclude='*svn*' --exclude='*.xml' org|tar x -C build/classes/
	#tar c -C src --exclude='*svn*' --exclude='*.xml' org|tar x -C WebContent/WEB-INF/classes/

package-jar:
	JAVA_HOME=/opt/jdk1.6.0_29 ant package-jar

publish:
	scp ~/csvalidation.war magivp:webapps/

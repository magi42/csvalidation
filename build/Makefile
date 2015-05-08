all: copy-src-to-classpath

copy-src-to-classpath:
	tar c -C src --exclude='*svn*' --exclude='*.xml' org|tar x -C build/classes/
	#tar c -C src --exclude='*svn*' --exclude='*.xml' org|tar x -C WebContent/WEB-INF/classes/

publish:
	scp ~/csvalidation.war magivp:webapps/

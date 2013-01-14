===========================
 Mobicents SS7 Protocols Bundle
===========================
               v2.1.0-SNAPSHOT

 Welcome to the Mobicents jSS7 Protocols Bundle v2.1.0-SNAPSHOT release! This is SNAPSHOT version. For stable binary builds look at http://code.google.com/p/jss7/wiki/Downloads?tm=2

--------------
 Introduction
--------------

This is the bundle release of Mobicents jSS7 stack.

In this bundle you will find the latest released Mobicents jSS7 Protocol components SCCP, TCAP, MAP, CAP, INAP and ISUP. Binary also includes mobicents-ss7-service which ties MTP3 to MAP stack together and binds MAP to JNDI; ready to be used by applications. In addition it includes Signaling Gateway (converting legacy SS7 MTP3 signal to M3UA over IP) and Mobicents SS7 Simulator to test the stack.

Complete list of issues covered in this release are listed here http://code.google.com/p/jss7/issues/list?can=1&q=Version%3D2.1.0.BETA1&sort=priority+version&groupby=version&colspec=Summary+Type+Status+ID&cells=tiles

Please note that archives are now uploaded to https://oss.sonatype.org/content/groups/public. Please point to sonatype repository in your applications pom.xml

				<repository>
                          <id>mobicents-public-repository-group</id>
                          <name>Mobicens Public Maven Repository Group</name>
                          <url>https://oss.sonatype.org/content/groups/public</url>
                          <layout>default</layout>
                          <releases>
                            <enabled>true</enabled>
                            <updatePolicy>never</updatePolicy>
                          </releases>
                          <snapshots>
                            <enabled>true</enabled>
                            <updatePolicy>never</updatePolicy>
                          </snapshots>
                </repository>


Daily build is configured at https://mobicents.ci.cloudbees.com/job/Mobicents-jSS7-2.x/



------------------
 Release Contents
------------------

  * ASN 2.0.0-SNAPSHOT
  * SCTP 1.1.0-SNAPSHOT
  * SS7 2.1.0-SNAPSHOT

  
------------------
Home page for each project 
------------------

http://code.google.com/p/jasn/
http://code.google.com/p/sctp/
http://code.google.com/p/jss7/

------------------
GIT Master Browse
------------------

http://code.google.com/p/jasn/source/browse/
http://code.google.com/p/sctp/source/browse/
http://code.google.com/p/jss7/source/browse/ 
  

  
----------------
 How to Use it?
----------------

Please refer to documentation in /ss7 module for detailed explanation.
 

------------------
 More Information
------------------

 For more detailed information, please visit the following sites:

  * Mobicents Google Group, for posting questions (http://groups.google.com/group/mobicents-public)

  * Mobicents Homepage (http://www.mobicents.org)


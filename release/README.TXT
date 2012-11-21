===========================
 Mobicents SS7 Protocols Bundle
===========================
               v2.0.0.BETA2

 Welcome to the Mobicents jSS7 Protocols Bundle v2.0.0.BETA2 release!

--------------
 Introduction
--------------

This is the bundle release of Mobicents jSS7 stack.

In this bundle you will find the latest released Mobicents jSS7 Protocol components SCCP, TCAP, MAP, CAP, INAP and ISUP. Binary also includes mobicents-ss7-service which ties MTP3 to MAP stack together and binds MAP to JNDI; ready to be used by applications. In addition it includes Signaling Gateway (converting legacy SS7 MTP3 signal to M3UA over IP) and Mobicents SS7 Simulator to test the stack.

------------------
What's new in this release?
------------------
This release is full of new features

 * The client side command line interface provides tab completion and help for every command
 * CAP V2 circuit switched call fully implemented
 * New MAP messages CheckIMEI implemented
 * All the unimplemented MAP/CAP messages have API interfaces allready defined  
 * M3UA ASP ID is can now be configured at ASP creation time
 * SCTP layer supports configuring server that accepts connection from anonymous ip:port 
 * Support for WinOS native drivers for Dialog  
 * Enhanced SS7 simulator to act as SMS Center for testing 

In addition many bugs fixed and some performance improvements.

Complete list of issues covered in this release are listed here http://code.google.com/p/jss7/issues/list?can=1&q=Version%3D2.0.0.BETA2&sort=priority+version&groupby=version&colspec=Summary+Type+Status+ID&cells=tiles

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

  * ASN 2.0.1.FINAL
  * SCTP 1.0.0.CR4
  * SS7 2.0.0.BETA2

  
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
  
------------------
 GIT Tag Browse
------------------

http://code.google.com/p/jasn/source/browse/?name=release-2.0.0.FINAL
http://code.google.com/p/sctp/source/browse/?name=release-1.0.0.CR3
http://code.google.com/p/jss7/source/browse/?name=release-2.0.0.BETA1


  
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


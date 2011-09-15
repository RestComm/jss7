This is simple load test for MAP.
To run test follow the steps
1) Modify the parameter <arg value="100" /> and <arg value="2" /> to suit your test. The first parameter is number of complete MAP calls the test should make and second parameter is number of concurrent MAP Dialogs the test should maintain. If concurrent MAP Dialog goes above threshold limit, test will throttle the creation of more Dialog till concurrent MAP Dialog again comes down below 50% of threshold specified.
2) Clean the previous execution logs by calling "ant clean"
3) Start server by calling "ant server"
4) Start client by calling "ant client"

Note : The build depends on property M2_HOME which points to your .m2/repository. This property is defined in map/map-impl/ant-build-config.properties. Please make sure you change it to point to correct repository loally

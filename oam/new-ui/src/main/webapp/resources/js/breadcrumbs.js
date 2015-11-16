		           	function generateMenu(m3uaName,title){
		           		crumbList.add(m3uaName,title);
		           		$('#breadcrumbs').children('li').remove();
		           		crumbList.output();
		           	}
		           	
		           	function CrumbList(initFunc,iniText){
		           	  this.links=new Array();
		           	  this.text=new Array();
		           	  this.add = crumbListAdd;
		           	  this.output = crumbListShow;
		              this.links[this.links.length]=initFunc;
		              this.text[this.text.length]=iniText;
		           	}
		            function crumbListAdd(href,text){
		            	
		            	var functionText = "showInternalPageDetails('"+href+"','"+text+"')";
		            	var removePos = this.links.length ;
		            	for(var i in this.links){
		            		if(this.links[i]==functionText){
		            			removePos = i;
		            			break;
		            		}
		            	}
		            	var linksTemp=new Array();
			           	var textTemp =new Array();
			           	
		            	for(var i =0;i<removePos ;i++){
		            		linksTemp[i] = this.links[i];
		            		textTemp[i]= this.text[i];
		            	}
		            	this.links = linksTemp;
		            	this.text = textTemp;
		            	
		                this.links[this.links.length]=functionText;
		                this.text[this.text.length]=text;
		            }
		            function crumbListShow(){
		            	  var menu = "";
			              for(var i in this.links){			            	
			                if(i==this.links.length-1){
			                	menu += ("<li><a href=\"#\"     "+ ((i%2==1)?" class=\"odd\" ":"  ")+" ><b> "+this.text[i]+" </b> "+ ((i%2==1)?"  <span class=\"arrow\"></span>":"  ")+"   </a></li>" );
			                }else{
			                	menu += ("<li><a href=\"#\" onclick=\""+this.links[i]+"\"  "+ ((i%2==1)?" class=\"odd\" ":"  ")+" >"+this.text[i]+"   <span class=\"arrow\"></span> </a></li>"
			                  );
			                }
			              }
			              $('#breadcrumbs').append(menu);
		            }
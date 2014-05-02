

package cloudoutput.extensions;

import cloudoutput.utils.FileIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class ExtensionsLoader {
    
    private static final Class<?>[] parameters = new Class[]{URL.class};
    
  
    private static List<String> addedFiles = new ArrayList<String>();
    
 
    private static String extensionsPath = FileIO.getPathOfExecutable() + "extensions";
    
   
    private static String classnamesXmlPath = FileIO.getPathOfExecutable() + "extensions/classnames.xml";
    
    
    private static Document classnamesXml = getClassnamesXml();

 
    private static void addFile(String canonicalPath) throws IOException {
        if(addedFiles.contains(canonicalPath)) return;
        
        File f = new File(canonicalPath);
        addURL(f.toURI().toURL());
        addedFiles.add(canonicalPath);
    }
    private static void addURL(URL url) throws IOException {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader,new Object[]{ url }); 
        } catch (Throwable t) {
            throw new IOException("Error: could not add URL to system classloader");
        }
    }
    public static List<String> getExtensionsAliasesByType(String type) {
        List<String> listAliases = new ArrayList<String>();
        if(classnamesXml == null) return listAliases;
        
        NodeList classNodes = classnamesXml.getElementsByTagName("class");            
        for(int index = 0; index < classNodes.getLength(); index++) {
        	try {
        		if(classNodes.item(index).getAttributes().getNamedItem("type").getNodeValue().equals(type))
        			listAliases.add(classNodes.item(index).getAttributes().getNamedItem("alias").getNodeValue());
        	}
        	catch (NullPointerException e) {
        		continue;
                }
        }
        
        return listAliases;
    }
    public static Object getExtension(String type, String alias, Class<?>[] constructorTypes, Object[] constructorArguments) {
        String filename = getFileName(type, alias);
        String classname = getClassnameOfExtension(type, alias);
        if(filename == null || classname == null) return null;        
        
        File extensionFile = new File(extensionsPath + "/" + filename);
        if(!extensionFile.exists() || !extensionFile.isFile()) return null;
        
        try {
            addFile(extensionFile.getCanonicalPath());
            
            if(constructorTypes != null && constructorArguments != null) 
                return ClassLoader.getSystemClassLoader().loadClass(classname).getConstructor(constructorTypes).newInstance(constructorArguments);            
            
            else return ClassLoader.getSystemClassLoader().loadClass(classname).getConstructor().newInstance();
            
        } catch (Exception ex) {
            Logger.getLogger(ExtensionsLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    private static String getFileName(String type, String alias) {
        if(classnamesXml == null) return null;
        
        NodeList classNodes = classnamesXml.getElementsByTagName("class");      
        for(int index = 0; index < classNodes.getLength(); index++) {
            if(classNodes.item(index).getAttributes().getNamedItem("type").getNodeValue().equals(type) &&
               classNodes.item(index).getAttributes().getNamedItem("alias").getNodeValue().equals(alias))
                return classNodes.item(index).getAttributes().getNamedItem("filename").getNodeValue();
        }
        
        return null;
    }
    private static String getClassnameOfExtension(String type, String alias) {
        if(classnamesXml == null) return null;
        
        NodeList classNodes = classnamesXml.getElementsByTagName("class");            
        for(int index = 0; index < classNodes.getLength(); index++) {
            if(classNodes.item(index).getAttributes().getNamedItem("type").getNodeValue().equals(type) &&
               classNodes.item(index).getAttributes().getNamedItem("alias").getNodeValue().equals(alias))
                return classNodes.item(index).getAttributes().getNamedItem("name").getNodeValue();
        }
        
        return null;
    }
    private static Document getClassnamesXml() {
        File classnamesXmlFile = new File(classnamesXmlPath);
        if(!classnamesXmlFile.exists() || !classnamesXmlFile.isFile()) {
            return null;
        }
        
        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            InputSource is = new InputSource(classnamesXmlFile.toURI().toURL().openStream());
            Document classnamesXmlDocument = db.parse(is);
            return classnamesXmlDocument;
            
        } catch (Exception ex) {
            Logger.getLogger(ExtensionsLoader.class.getName()).log(Level.INFO, null, ex);
            return null;
        }
    }
        
}

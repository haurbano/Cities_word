package salt.movil.testsoap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import salt.movil.testsoap.background.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Request.Cities {

    Button btn;
    TextView txt;
    EditText edit;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        txt = (TextView) findViewById(R.id.txt);
        edit = (EditText) findViewById(R.id.editPais);
        list = (ListView) findViewById(R.id.list_cities);

        btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Request r = new Request(this);
        r.execute(edit.getText().toString());
    }


    @Override
    public void setObjetXML(SoapPrimitive sp) throws IOException, SAXException, ParserConfigurationException {
        readXml(sp);
    }

    private void readXml(SoapPrimitive sp) throws ParserConfigurationException, IOException, SAXException {
        List<String> cities= new ArrayList<>();
        String xml = sp.toString();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        Document doc =  builder.parse(in, null);
        NodeList nodeList = doc.getElementsByTagName("City");

        for (int i=0;i< nodeList.getLength();i++){
            Element element = (Element) nodeList.item(i);
            String city = element.getTextContent();
            cities.add(city);
        }

        int c = nodeList.getLength();
        txt.setText("Numero de Ciudades: " + c);
        showCities(cities);
    }

    private void showCities(List<String> data){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,data);
        list.setAdapter(arrayAdapter);
    }
}

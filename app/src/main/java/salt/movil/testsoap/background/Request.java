package salt.movil.testsoap.background;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by usuario on 10/03/2016.
 */
public class Request extends AsyncTask<String, Integer, SoapPrimitive> {

    public interface Cities
    {
        void setObjetXML(SoapPrimitive sp) throws IOException, SAXException, ParserConfigurationException;
    }

    Cities cities;

    public Request(Cities cities){
        this.cities = cities;
    }

    // Metodo que queremos ejecutar en el servicio web
    private static final String metodo = "GetCitiesByCountry";
    // Namespace definido en el servicio web
    private static final String namespace = "http://www.webserviceX.NET";
    // namespace + metodo
    private static final String accionSoap = "http://www.webserviceX.NET/GetCitiesByCountry";
    // Fichero de definicion del servcio web
    private static final String url = "http://www.webservicex.net/globalweather.asmx";

    @Override
    protected SoapPrimitive doInBackground(String... params) {

        SoapPrimitive resultado;
        SoapObject request = new SoapObject(namespace,metodo);
        request.addProperty("CountryName", params[0]);

        SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sobre.dotNet = true;
        sobre.implicitTypes = true;
        sobre.setAddAdornments(false);
        sobre.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(url);

        try {
            transporte.call(accionSoap, sobre);
            resultado = (SoapPrimitive) sobre.getResponse();
            return resultado;
        } catch (IOException e) {
            Log.i("haur", "Error:" + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            Log.i("haur","Error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }


    }

    @Override
    protected void onPostExecute(SoapPrimitive soapPrimitive) {
        try {
            cities.setObjetXML(soapPrimitive);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        super.onPostExecute(soapPrimitive);
    }
}

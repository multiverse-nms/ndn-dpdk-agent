package nms.forwarder.model.face;


import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;
import net.named_data.jndn.ComponentType;
import net.named_data.jndn.ContentType;
import net.named_data.jndn.Data;
import net.named_data.jndn.MetaInfo;
import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.Tlv;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encrypt.Schedule;
import net.named_data.jndn.security.ValidityPeriod;
import net.named_data.jndn.util.Blob;
import nms.restclient.Route;
import net.named_data.jndn.encoding.Tlv0_3WireFormat;


public class PrefixAnnouncement {
	
	private static final Logger LOG = LoggerFactory.getLogger(PrefixAnnouncement.class);
	private Data m_data = new Data();
	private Name m_announcedName = new Name();
	private double m_expiration = 0; //int
	private ValidityPeriod m_validity = new ValidityPeriod();
	private static String KEYWORD_PA_COMP = "32=PA"; // = "20 02 5041"_block; //32=PA
	//Blob value = new Blob("32=PA");
	private int ContentType_PrefixAnn = 5;	
		public byte[] hexStringToByteArray(String s) {
			byte[] b = new byte[s.length() / 2];
		    for (int i = 0; i < b.length; i++) {
		      int index = i * 2;
		      int v = Integer.parseInt(s.substring(index, index + 2), 16);
		      b[i] = (byte) v;
		    }
		    return b;
		}	
		
		
		
//	Data
//	  Name /net/example/32=PA/%FD%01/%00%00
//	  MetaInfo
//	    ContentType 5 (prefix announcement)
//	  Content
//	    ExpirationPeriod 3600000
//	    ValidityPeriod
//	      NotBefore 20181030T000000
//	      NotAfter  20181124T235959
//	  SignatureInfo
//	  SignatureValue 


	public Data getData() {
		return m_data;
	}

	public Name getAnnouncedName() {
		return m_announcedName;
	}

	public void setValidityPeriod(ValidityPeriod validity) {
		this.m_validity = validity;
	}

	public ValidityPeriod getValidityPeriod() {
		return m_validity;
	}

	public void setAnnouncedName(Name announcedName) {
		this.m_announcedName = announcedName;
	}

	public double getExpiration() {
		return m_expiration;
	}

	public void setExpiration(double expiration) {
		this.m_expiration = expiration;
	}

	
	public void decodePrefixAnnouncement(PrefixAnnouncement pa, ByteBuffer input, boolean copy)
			throws EncodingException {

		pa.clear();

		TlvDecoder decoder = new TlvDecoder(input);

		int endOffset = decoder.readNestedTlvsStart(Tlv.Data);  

		// decode name
		if (decoder.peekType(Tlv.Name, endOffset)) {
			Name name = new Name();
			Tlv0_3WireFormat.decodeName(name, new int[1], new int[1], decoder, copy);
			pa.setAnnouncedName(name);
			LOG.debug("decodedName = {}", pa.getAnnouncedName());
			LOG.debug("decodedPrefix = {}", pa.getAnnouncedName().getPrefix(-3));
		}
		// decode metaInfo 
		// check the content type is 5
		if (decoder.peekType(Tlv.MetaInfo, endOffset)) {
			decodeMetaInfo(pa.getData().getMetaInfo(), decoder, copy);
		}
		if (pa.getData().getMetaInfo().getOtherTypeCode() != ContentType_PrefixAnn) {
			LOG.debug("Data is not a prefix announcement"); }
		
		pa.getData().setContent(new Blob(decoder.readOptionalBlobTlv(Tlv.Content, endOffset), copy));
		if (pa.getData().getContent().size() == 0) {
			  System.out.println("Prefix announcement is empty"); }
		 Blob payload = pa.getData().getContent();
		 decoder = new TlvDecoder(payload.buf());
		// decode expiration period
		 m_expiration = decoder.readOptionalNonNegativeIntegerTlv(Tlv.ControlParameters_ExpirationPeriod,endOffset);
		 LOG.debug("m_expiration = {}", m_expiration);
		 pa.setExpiration(m_expiration);
		
		// decode validity period
		if (decoder.peekType(Tlv.ValidityPeriod_ValidityPeriod, endOffset)) {
			decodeValidityPeriod(m_validity, decoder);}
			pa.setValidityPeriod(m_validity);
			
			DateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS Z");
			Date result = new Date((long)m_validity.getNotAfter());
			
			DateFormat simplee = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS Z");
			Date resultt = new Date((long)m_validity.getNotBefore());
			

			LOG.debug("ValidityPeriod_NotBefore = {}", simple.format(result));
			LOG.debug("ValidityPeriod_NotBefore = {}", simplee.format(resultt));			
		// decode signedInfo and value
		decodeSignatureInfo(pa.getData(), decoder, copy);

		//int signedPortionEndOffset = decoder.getOffset();
		//pa.getData().getSignature().setSignature(new Blob(decoder.readBlobTlv(Tlv.SignatureInfo), copy));

		//decoder.finishNestedTlvs(endOffset, true);
		
		  }
	

	
	
	
	private static void decodeSignatureInfo(Data data, TlvDecoder decoder, boolean copy) {
		// TODO Auto-generated method stub

	}

	private void clear() {
		// TODO Auto-generated method stub

	}

	private static void decodeValidityPeriod(ValidityPeriod validityPeriod, TlvDecoder decoder)
			throws EncodingException {
		int endOffset = decoder.readNestedTlvsStart(Tlv.ValidityPeriod_ValidityPeriod);

		validityPeriod.clear();

		// Set copy false since we just immediately get the string.
		Blob isoString = new Blob(decoder.readBlobTlv(Tlv.ValidityPeriod_NotBefore), false);
		double notBefore = Schedule.fromIsoString("" + isoString);
		isoString = new Blob(decoder.readBlobTlv(Tlv.ValidityPeriod_NotAfter), false);
		double notAfter = Schedule.fromIsoString("" + isoString);

		validityPeriod.setPeriod(notBefore, notAfter);

		decoder.finishNestedTlvs(endOffset);
	}
	public static int convertToMillis(double timeInSeconds) {
        return (int) (timeInSeconds * 1000f);
    }

	private static void decodeMetaInfo(MetaInfo metaInfo, TlvDecoder decoder, boolean copy) throws EncodingException {
		int endOffset = decoder.readNestedTlvsStart(Tlv.MetaInfo);

		// The ContentType enum is set up with the correct integer for each
		// NDN-TLV ContentType.
		int type = (int) decoder.readOptionalNonNegativeIntegerTlv(Tlv.ContentType, endOffset);
		if (type < 0 || type == ContentType.BLOB.getNumericType())
			// Default to BLOB if the value is omitted.
			metaInfo.setType(ContentType.BLOB);
		else if (type == ContentType.LINK.getNumericType())
			metaInfo.setType(ContentType.LINK);
		else if (type == ContentType.KEY.getNumericType())
			metaInfo.setType(ContentType.KEY);
		else if (type == ContentType.NACK.getNumericType())
			metaInfo.setType(ContentType.NACK);
		else {
			// Unrecognized content type.
			metaInfo.setType(ContentType.OTHER_CODE);
			metaInfo.setOtherTypeCode(type);
		}

		metaInfo.setFreshnessPeriod(decoder.readOptionalNonNegativeIntegerTlv(Tlv.FreshnessPeriod, endOffset));
		if (decoder.peekType(Tlv.FinalBlockId, endOffset)) {
			int finalBlockIdEndOffset = decoder.readNestedTlvsStart(Tlv.FinalBlockId);
			metaInfo.setFinalBlockId(decodeNameComponent(decoder, copy));
			decoder.finishNestedTlvs(finalBlockIdEndOffset);
		} else
			metaInfo.setFinalBlockId(null);

		decoder.finishNestedTlvs(endOffset);
	}

	private static Name.Component decodeNameComponent(TlvDecoder decoder, boolean copy) throws EncodingException {
		int savePosition = decoder.getOffset();
		int type = decoder.readVarNumber();
		// Restore the position.
		decoder.seek(savePosition);

		Blob value = new Blob(decoder.readBlobTlv(type), copy);
		if (type == Tlv.ImplicitSha256DigestComponent)
			return Name.Component.fromImplicitSha256Digest(value);
		else if (type == Tlv.ParametersSha256DigestComponent)
			return Name.Component.fromParametersSha256Digest(value);
		else if (type == Tlv.NameComponent)
			return new Name.Component(value);
		else
			// Unrecognized type code.
			return new Name.Component(value, ComponentType.OTHER_CODE, type);
	}
	
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		
		return json;
	}

public static void main(String[] args) {
		
		PrefixAnnouncement pa = new PrefixAnnouncement();
		
		byte[] todecode = pa.hexStringToByteArray("0678071808036E657408076578616D706C6520025041230101210100140318010515306D040036EE80FD00FD26FD00FE0F323031383130333054303030303030FD00FF0F32303138313132345432333539353916031B010017200000000000000000000000000000000000000000000000000000000000000000");
		
		ByteBuffer buffer = ByteBuffer.wrap(todecode);
		
		
		try {
			pa.decodePrefixAnnouncement(pa, buffer, true);
		} catch (EncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}
}
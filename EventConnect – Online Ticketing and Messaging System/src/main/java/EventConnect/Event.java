/*
 * This class is to:
 * 1. Create Event and insert its info to Events table
 */

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Event {
	private int id;
    private String name;
    private String location;
    private String date; 
    private String details;
    private String QRCodePath;
    
    Event(String name, String location, String date, String details) throws WriterException, IOException{
    	EventJDBCConnector auth = new EventJDBCConnector();
    	this.id = auth.getEventID(name);
    	this.name = name;
    	this.location = location;
    	this.date = date;
    	this.details = details;
    	this.QRCodePath = generateQR(String.valueOf(id));
    	auth.insertEventToSQL(name, location,date, details, QRCodePath);
    }
    
    // Scanning QR code will direct the user to the corresponding event html webpage.
    // current Events:
    /* CREATE TABLE Events (
		    eventID INT AUTO_INCREMENT PRIMARY KEY,
		    eventName VARCHAR(255) UNIQUE NOT NULL,
		    location VARCHAR(255) NOT NULL,
		    date DATETIME NOT NULL,
		    description TEXT,
		    QRCodePath VARCHAR(255) UNIQUE NOT NULL  -- Storing the file path
		);
     * INSERT INTO Events (eventName, location, date, description, QRCodePath) VALUES
		('Summer Music Festival', 'New York City, NY', '2024-06-21 14:00:00', 'Join us for a day filled with music from top artists and local bands.', '/qr_codes/summer_music_festival.png'),
		('Tech Conference 2024', 'San Francisco, CA', '2024-09-15 09:00:00', 'A comprehensive conference featuring industry leaders in technology and innovation.', '/qr_codes/tech_conference_2024.png'),
		('Marathon 2024', 'Boston, MA', '2024-04-19 06:00:00', 'Annual marathon with participants from all over the world.', '/qr_codes/marathon_2024.png'),
		('Food Expo 2024', 'Chicago, IL', '2024-08-12 10:00:00', 'Explore culinary delights from famous chefs and local favorites.', '/qr_codes/food_expo_2024.png'),
		('Art & Sculpture Exhibition', 'Los Angeles, CA', '2024-11-05 11:00:00', 'Experience breathtaking contemporary art and sculptures by renowned artists.', '/qr_codes/art_sculpture_exhibition.png');
	*/
    private String generateQR(String eventID) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(eventID, BarcodeFormat.QR_CODE, 350, 350);

        String filePath = "QRCode_" + eventID + ".png";
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return filePath; 
    }

    public String getQRPath() {
    	return QRCodePath;
    }
    
    public int getID() {
    	return id;
    }
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public String getLocation(){
        return location;
    }
    
    public void setLocation(String location){
        this.location = location;
    }

    public String getDate(){
        return date;
    }
    
    public void setDate(String date){
        this.date = date;
    }

    public String getDetails(){
        return details;
    }
    
    public void setDetails(String details){
        this.details = details;
    }
}
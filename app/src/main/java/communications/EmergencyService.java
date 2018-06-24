package communications;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmergencyService extends Service {
    public EmergencyService() {
    }


    private String myLocationAddressName = "" ,phone;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // if(parameterid == "B0199")
        //{



            phone =intent.getStringExtra("Phone");

            getLocation();

        //}



        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    DatabaseReference reference;
    private void getLocation(){

        String User_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(User_id).child("CurrentAddress").child(User_id).child("l");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    List<Object> map =(List<Object>) dataSnapshot.getValue();
                    double LocationLat=0;
                    double LocationLng=0;


                    if(map.get(0) !=null )
                    {

                        LocationLat = Double.parseDouble(map.get(0).toString());
                    }

                    if(map.get(1) !=null )
                    {

                        LocationLng = Double.parseDouble(map.get(1).toString());
                    }

                    Geocoder geocoder=new Geocoder(EmergencyService.this, Locale.getDefault());

                    try {
                        List<Address> myList = geocoder.getFromLocation(LocationLat,LocationLng, 1);

                        Address address = (Address) myList.get(0);
                        myLocationAddressName=address.getAddressLine(0).toString();
                        if(myLocationAddressName!=null) {
                            sendSMS(phone, myLocationAddressName+"\n Emergency Situation");
                            reference.removeEventListener(this);
                            stopSelf();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void sendSMS(String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}

package zconnectcom.zutto.zconnectshophandle.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class statIncrement {
    private String stat;

    public statIncrement(String stat) {
        this.stat = stat;
    }

    public void change(final boolean increment) {
        final DatabaseReference mStats = FirebaseDatabase.getInstance().getReference("Stats").child(stat);
        mStats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int newStat = Integer.parseInt((String) dataSnapshot.getValue());
                newStat = increment ? newStat + 1 : newStat - 1;
                mStats.setValue(String.valueOf(newStat));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

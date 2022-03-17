package haohuynh.st.ueh.edu.articleapp;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ArticleData {
    private static Context context;
    private static GridView gridView;
    private static ArrayList<Article> data = new ArrayList<Article>();
    public ArticleData(Context context, GridView gridView){
        this.context = context;
        this.gridView = gridView;
        readDataFromDB();
    }

    public static Article getPhotoFromId(int id) {
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).getArticle_id() == id)
                return data.get(i);
        return null;
    }

    public static void readDataFromDB()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance(context.getString(R.string.database_url));
        DatabaseReference dbref = database.getReference("Article");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Article article = postSnapshot.getValue(Article.class);
                    data.add(article);
                }
                displayOnGridView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private static void displayOnGridView()
    {
        ArticleAdapter articleAdapter = new ArticleAdapter(data, context);
        gridView.setAdapter(articleAdapter);
    }
}
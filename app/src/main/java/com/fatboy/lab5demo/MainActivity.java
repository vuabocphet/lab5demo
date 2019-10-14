package com.fatboy.lab5demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.lvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update Recyclerview
                adapter.setNotes(notes);
                adapter.setMainActivity(MainActivity.this);
            }
        });
    }

    public void btnAdd(View view) {
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
        this.startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            String title=data.getStringExtra(Main2Activity.EXTRA_TITLE);
            String des=data.getStringExtra(Main2Activity.EXTRA_DESCRIPTION);
            Note note=new Note(title,des);
            noteViewModel.insert(note);
            Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();

        }

        if (requestCode==2 && resultCode==RESULT_OK){
            String title=data.getStringExtra(Main2Activity.EXTRA_TITLE);
            String des=data.getStringExtra(Main2Activity.EXTRA_DESCRIPTION);

            Note note=new Note(title,des);
            Log.e("NOTE",note.getTitle()+"-"+note.getDescription());
            update(note);


        }
    }

    public void update(Note note){
        noteViewModel.update(note);
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    public void updatenote(Note note) {
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
        intent.putExtra("DATA",note);
        this.startActivityForResult(intent,2);
    }



    public void delete(Note note){
        noteViewModel.delete(note);
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }


}

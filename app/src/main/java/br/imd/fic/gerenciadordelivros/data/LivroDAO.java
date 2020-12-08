package br.imd.fic.gerenciadordelivros.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.imd.fic.gerenciadordelivros.dominio.Livro;

public class LivroDAO {

    private SQLiteDatabase bd;
    private static LivroDAO instance;

    private LivroDAO(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        bd = dbHelper.getWritableDatabase();
    }

    public static LivroDAO getInstance(Context context) {
        if(instance == null) {
            instance = new LivroDAO(context.getApplicationContext());
        }

        return instance;
    }

    public List<Livro> list() {

        String[] columns = {
                LivroContract.Columns._ID,
                LivroContract.Columns.titulo,
                LivroContract.Columns.autor,
                LivroContract.Columns.editora,
                LivroContract.Columns.emprestado
        };

        List<Livro> livros = new ArrayList<>();

        try(Cursor c = bd.query(LivroContract.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            LivroContract.Columns.titulo)
        ){
            if(c.moveToFirst()) {
                do {
                    Livro l = LivroDAO.fromCursor(c);
                    livros.add(l);
                }while (c.moveToNext());
            }
        }

        return livros;
    }

    private static Livro fromCursor(Cursor c) {

        Long id = c.getLong(c.getColumnIndex(LivroContract.Columns._ID));
        String tituto = c.getString(c.getColumnIndex(LivroContract.Columns.titulo));
        String autor = c.getString(c.getColumnIndex(LivroContract.Columns.autor));
        String editora = c.getString(c.getColumnIndex(LivroContract.Columns.editora));
        int emprestado = c.getInt(c.getColumnIndex(LivroContract.Columns.emprestado));

        return new Livro(id, tituto, autor, editora, emprestado);
    }
}

package andre.com.br.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import andre.com.br.agenda.domain.Aluno;

/**
 * Created by Andre on 04/12/2017.
 */

public class AlunoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 3;
    private static final String TABELA = "Alunos";
    private static final String DATABASE = "CadastroAlunos";

    public AlunoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql =  new StringBuilder();
        sql.append(" CREATE TABLE "+ TABELA + " (");
        sql.append("id INTEGER PRIMARY KEY ");
        sql.append(",nome TEXT NOT NULL");
        sql.append(",telefone TEXT");
        sql.append(",site TEXT");
        sql.append(",endereco TEXT");
        sql.append(",nota REAL");
        sql.append(",caminhoFoto TEXT);");


        sqLiteDatabase.execSQL(sql.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            StringBuilder sql = new StringBuilder();
            sql.append("DROP TABLE IF EXISTS "+ TABELA );
            sqLiteDatabase.execSQL(sql.toString());
            onCreate(sqLiteDatabase);
        }
    }

    public void insere(Aluno aluno){
        ContentValues values = getContentValuesAlunos(aluno);

        getWritableDatabase().insert(TABELA,null,values);
    }

    public Aluno getByPhone(String phone){
        String sql = getSql("telefone = ? ").toString();
        List<Aluno> alunos =  ExecuteSql(sql,new String[]{phone});
        if(alunos.isEmpty())
            return null;

        return alunos.get(0);
    }

    @NonNull
    private ContentValues getContentValuesAlunos(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("telefone",aluno.getTelefone());
        values.put("site",aluno.getSite());
        values.put("endereco",aluno.getEndereco());
        values.put("nota",aluno.getNota());
        values.put("caminhoFoto",aluno.getCaminhoFoto());
        return values;
    }

    public void altera(Aluno aluno){
        ContentValues dados = getContentValuesAlunos(aluno);
        String[] param = {aluno.getId().toString()};
        getWritableDatabase().update(TABELA, dados,"id = ?",param);
    }

    public void deleta(Aluno aluno){

        String[] param = {aluno.getId().toString()};
        getWritableDatabase().delete(TABELA,"id = ?",param);
    }

    public List<Aluno> getLista(){

        StringBuilder sql = getSql(null);

        return ExecuteSql(sql.toString(), null);
    }

    private List<Aluno> ExecuteSql(String sql, String[] args) {
        List<Aluno> retorno = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(sql,args);

        while(cursor.moveToNext()){
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

            retorno.add(aluno);
        }
        cursor.close();

        return retorno;
    }

    @NonNull
    private StringBuilder getSql(String condicao) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id");
        sql.append(",nome");
        sql.append(",telefone");
        sql.append(",endereco");
        sql.append(",site");
        sql.append(",nota");
        sql.append(",caminhoFoto");
        sql.append(" FROM "+ TABELA);

        if(condicao != null)
            sql.append(" WHERE " + condicao + " ;");
        else
            sql.append(";");

        return sql;
    }

}

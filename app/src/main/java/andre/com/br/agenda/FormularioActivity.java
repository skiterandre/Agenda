package andre.com.br.agenda;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import andre.com.br.agenda.dao.AlunoDAO;
import andre.com.br.agenda.domain.Aluno;
import andre.com.br.agenda.helper.FormularioHelper;

public class FormularioActivity extends AppCompatActivity {

    public static final int CAMERA_CODE = 123;
    private FormularioHelper alunoHelper;
    private String caminhoFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        alunoHelper = new FormularioHelper(this);

        Intent intent = getIntent();
        Aluno aluno = (Aluno)intent.getSerializableExtra("aluno");
        if(aluno != null){
            alunoHelper.preencheFormulario(aluno);
        }

        Button btnFoto = alunoHelper.getFotoButton();
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(FormularioActivity.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(FormularioActivity.this,new String[]{
                            Manifest.permission.CAMERA
                    },1);
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = Environment.getExternalStorageDirectory()  + "/" + System.currentTimeMillis() + ".jpg";
                caminhoFoto = getExternalFilesDir(null)  + "/" + System.currentTimeMillis() + ".jpg";
                File file = new File(caminhoFoto);
                Uri outputFileUri = FileProvider.getUriForFile(FormularioActivity.this,
                        "andre.com.br.fileprovider",file);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode){
            case(CAMERA_CODE):
                alunoHelper.carregarImagem(caminhoFoto);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnSalvar:


                if(alunoHelper.temNome()){
                    Aluno aluno = alunoHelper.getAluno();
                    AlunoDAO dao = new AlunoDAO(this);
                    if(aluno.getId() == null){
                        dao.insere(aluno);
                    }else {
                        dao.altera(aluno);
                    }

                    dao.close();
                    Toast.makeText(FormularioActivity.this,"Aluno "+aluno.getNome()+" Salvo com Sucesso!",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    alunoHelper.mostraErro();
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

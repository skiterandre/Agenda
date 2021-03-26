package andre.com.br.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import andre.com.br.agenda.adapter.ListaAlunoAdapter;
import andre.com.br.agenda.converter.AlunoConverter;
import andre.com.br.agenda.dao.AlunoDAO;
import andre.com.br.agenda.domain.Aluno;
import andre.com.br.agenda.helper.FormularioHelper;

public class ListaAlunosActivity extends AppCompatActivity {

    public static final int REQUESTE_SMS_PERMISSION = 1;
    private ListView listaAlunos;
    private List<Aluno> alunos;
    private Button btnNovo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = findViewById(R.id.lista_alunos);
        registerForContextMenu(listaAlunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                Intent abrirFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                abrirFormulario.putExtra("aluno", aluno);
                startActivity(abrirFormulario);


            }
        });

        btnNovo = findViewById(R.id.lista_novo);
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaAlunosActivity.this,FormularioActivity.class);
                startActivity(intent);
            }
        });
        pedirPermissaoSmsReceive();
    }

    private void pedirPermissaoSmsReceive() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECEIVE_SMS
            }, REQUESTE_SMS_PERMISSION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final Aluno aluno = (Aluno)listaAlunos.getItemAtPosition(info.position);

        MenuItem ligar = menu.add("Ligar");
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    //Solicita Permissao para aplicação
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                }else{
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:"+ aluno.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });

        MenuItem sms = menu.add("Enviar SMS");
        Intent enviarSMS = new Intent(Intent.ACTION_VIEW);
        enviarSMS.setData(Uri.parse("sms:"+ aluno.getTelefone()));
        sms.setIntent(enviarSMS);

        MenuItem mapa = menu.add("Abrir no Mapa");
        Intent abrirMapa = new Intent(Intent.ACTION_VIEW);
        abrirMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        mapa.setIntent(abrirMapa);

        MenuItem site = menu.add("Abrir Site");
        Intent abrirSite = new Intent(Intent.ACTION_VIEW);

        String siteAluno = aluno.getSite();
        if(!siteAluno.startsWith("http://")){
            siteAluno = "http://" + siteAluno;
        }

        abrirSite.setData(Uri.parse(siteAluno));
        site.setIntent(abrirSite);


        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                Toast.makeText(ListaAlunosActivity.this,"Contato "+ aluno.getNome() + " deletado",Toast.LENGTH_SHORT).show();
                carregaLista();
                return false;
            }
        });

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_enviar_notas:

                EnviaAlunosTask task = new EnviaAlunosTask(this);
                task.execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregaLista(){

        alunos = PesquisaListaAlunos();

        ListaAlunoAdapter adapter = new ListaAlunoAdapter(this,alunos);
        listaAlunos.setAdapter(adapter);

    }

    private List<Aluno> PesquisaListaAlunos() {

        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> lista = dao.getLista();
        dao.close();
        return lista;
    }
}

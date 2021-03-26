package andre.com.br.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import andre.com.br.agenda.converter.AlunoConverter;
import andre.com.br.agenda.dao.AlunoDAO;
import andre.com.br.agenda.domain.Aluno;

public class EnviaAlunosTask extends AsyncTask<Object,String,String> {

    private Context context;
    private ProgressDialog dialog;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde!", "Enviando Alunos...",true,false);

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... objects) {
        AlunoDAO daoAluno = new AlunoDAO(context);
        List<Aluno> listaAlunos = daoAluno.getLista();
        AlunoConverter converter = new AlunoConverter();
        String json = converter.converteParaJSON(listaAlunos);

        WebClient client = new WebClient();
        String retorno = client.post(json);
        return retorno;

    }

    @Override
    protected void onPostExecute(String resposta) {
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
        dialog.dismiss();
        super.onPostExecute(resposta);
    }
}

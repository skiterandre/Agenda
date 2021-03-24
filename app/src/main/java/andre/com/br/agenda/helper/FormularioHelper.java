package andre.com.br.agenda.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import andre.com.br.agenda.FormularioActivity;
import andre.com.br.agenda.R;
import andre.com.br.agenda.domain.Aluno;

/**
 * Created by Andre on 30/11/2017.
 */

public class FormularioHelper {
    private Aluno aluno;
    private EditText nome;
    private EditText telefone;
    private EditText site;
    private EditText endereco;
    private RatingBar nota;
    private ImageView foto;
    private Button fotoButton;


    public FormularioHelper(FormularioActivity activity){
        nome = activity.findViewById(R.id.form_nome);
        telefone = activity.findViewById(R.id.form_telefone);
        site = activity.findViewById(R.id.form_site);
        endereco = activity.findViewById(R.id.form_endereco);
        nota = activity.findViewById(R.id.form_nota);
        foto = activity.findViewById(R.id.form_foto);
        fotoButton = activity.findViewById(R.id.form_botao_foto);
        aluno = new Aluno();

    }

    public Aluno getAluno(){

        aluno.setNome(nome.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setNota(Double.valueOf(nota.getProgress()));
        aluno.setCaminhoFoto((String)foto.getTag());

        return this.aluno;
    }

    public void preencheFormulario(Aluno aluno) {
        nome.setText(aluno.getNome());
        telefone.setText(aluno.getTelefone());
        site.setText(aluno.getSite());
        endereco.setText(aluno.getEndereco());
        nota.setProgress(aluno.getNota().intValue());
        carregarImagem(aluno.getCaminhoFoto());
        this.aluno = aluno;
    }

    public Button getFotoButton() {
        return fotoButton;
    }

    public boolean temNome(){
        return !nome.getText().toString().isEmpty();
    }

    public void mostraErro(){
        nome.setError("Campo Nome não pode ser vazio!");
    }

    public void carregarImagem(String localArquivoFoto){
        if(localArquivoFoto != null){
            Bitmap imagemFoto = BitmapFactory.decodeFile(localArquivoFoto);
            Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto,imagemFoto.getWidth(),300,true);
            foto.setImageBitmap(imagemFotoReduzida);
            foto.setTag(localArquivoFoto);
            foto.setScaleType(ImageView.ScaleType.FIT_XY);
            aluno.setCaminhoFoto(localArquivoFoto);
        }
    }
}

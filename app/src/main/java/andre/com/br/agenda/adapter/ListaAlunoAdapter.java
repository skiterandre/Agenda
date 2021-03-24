package andre.com.br.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import andre.com.br.agenda.R;
import andre.com.br.agenda.domain.Aluno;

/**
 * Created by Andre on 17/10/2018.
 */

public class ListaAlunoAdapter extends BaseAdapter {

    private final List<Aluno> alunos;
    private final Context context;

    public ListaAlunoAdapter(Context context,List<Aluno> alunos){
        this.alunos = alunos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.alunos.size();
    }

    @Override
    public Object getItem(int i) {
        return this.alunos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.alunos.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parentView) {

        Aluno aluno = this.alunos.get(i);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.lista_item, parentView, false);
        }

        TextView txtNome = view.findViewById(R.id.item_nome);
        txtNome.setText(aluno.getNome());

        TextView txtTelefone = view.findViewById(R.id.item_telefone);
        txtTelefone.setText(aluno.getTelefone());

        ImageView imgFoto = view.findViewById(R.id.lista_foto);
        String caminhoFoto = aluno.getCaminhoFoto();

        if(caminhoFoto != null){
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap,100,100,true);
            imgFoto.setImageBitmap(bitmapReduzido);
            imgFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }
}

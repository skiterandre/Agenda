package andre.com.br.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import andre.com.br.agenda.domain.Aluno;

public class AlunoConverter {


    public String converteParaJSON(List<Aluno> lista){
        JSONStringer js = new JSONStringer();

        try {
            js.object().key("list").array().object().key("aluno").array();

            for(Aluno aluno: lista){
                js.object();
                js.key("id").value(aluno.getId());
                js.key("nome").value(aluno.getNome());
                js.key("nota").value(aluno.getNota());
                js.endObject();
            }

            js.endArray().endObject().endArray().endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return js.toString();
    }
}

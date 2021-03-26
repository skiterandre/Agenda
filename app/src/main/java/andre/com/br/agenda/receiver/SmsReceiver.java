package andre.com.br.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import andre.com.br.agenda.R;
import andre.com.br.agenda.dao.AlunoDAO;
import andre.com.br.agenda.domain.Aluno;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        String formato = (String) intent.getSerializableExtra("format");
        SmsMessage sms = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            sms = SmsMessage.createFromPdu(pdu, formato);
        }

        if(sms != null){
            String telefone = sms.getDisplayOriginatingAddress();

            AlunoDAO daoAluno = new AlunoDAO(context);

            Aluno aluno = daoAluno.getByPhone(telefone);
            daoAluno.close();
            if(aluno != null){
                Toast.makeText(context, "Chegou um novo SMS do: " + aluno.getNome(), Toast.LENGTH_LONG).show();
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.msg);
                mediaPlayer.start();
            }
        }
    }
}

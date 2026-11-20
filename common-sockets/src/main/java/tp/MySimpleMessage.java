package tp;

import java.io.*;

//dans le contexte de ce TP les messages echangés auront toujours cette structure commune
//paquet d'octets avec dans l'ordre:
// les "2 + n" premiers octets = chaine de caractère java
// encodée via DataOutputStream.writeUTF() et a décoder via DataInputStream.readUTF()
// nom ou valeur simple de message transmis (requete ou réponse)
//si suite à analyser/écrire --> dans autre classe
public class MySimpleMessage {


    public static String readSimpleMessage(DataInputStream dataInputStream) throws EndReadLoopException{
        String message=null;
        try {
            message = dataInputStream.readUTF();
        } catch (EOFException e) {
            throw new EndReadLoopException();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }



    public static void writeSimpleMessage(DataOutputStream dataOutputStream , String message){
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

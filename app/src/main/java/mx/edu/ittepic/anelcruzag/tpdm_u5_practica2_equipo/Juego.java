package mx.edu.ittepic.anelcruzag.tpdm_u5_practica2_equipo;

public class Juego {
    String sobre1, sobre2, num1, num2, objeto1, objeto2, ready1, ready2, finish,reset;

    public Juego(String sobre1, String sobre2, String num1, String num2, String objeto1, String objeto2, String ready1, String ready2, String finish, String reset) {
        this.sobre1 = sobre1;
        this.sobre2 = sobre2;
        this.num1 = num1;
        this.num2 = num2;
        this.objeto1 = objeto1;
        this.objeto2 = objeto2;
        this.ready1 = ready1;
        this.ready2 = ready2;
        this.finish = finish;
        this.reset = finish;

    }// constructor

    public Juego(){

    }

    public String getSobre1() {
        return sobre1;
    }

    public void setSobre1(String sobre1) {
        this.sobre1 = sobre1;
    }

    public String getSobre2() {
        return sobre2;
    }

    public void setSobre2(String sobre2) {
        this.sobre2 = sobre2;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getObjeto1() {
        return objeto1;
    }

    public void setObjeto1(String objeto1) {
        this.objeto1 = objeto1;
    }

    public String getObjeto2() {
        return objeto2;
    }

    public void setObjeto2(String objeto2) {
        this.objeto2 = objeto2;
    }

    public String getReady1() {
        return ready1;
    }

    public void setReady1(String ready1) {
        this.ready1 = ready1;
    }

    public String getReady2() {
        return ready2;
    }

    public void setReady2(String ready2) {
        this.ready2 = ready2;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}

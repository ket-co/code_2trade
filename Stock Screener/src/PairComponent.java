import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
class PairComponent{
    static int errorCounter=0;
    static int currentXPos=5,currentYPos=150;
    static int prevXPos=5,prevYPos=150;
    boolean isError=false;
    int widthSpacing=30,heightSpacing=40,fontSize=30,labelWidth,textFieldWidth;
    JLabel label;
    JTextField textField;
    Color labelBackColor,labelForeColor,tfBackColor,tfForeColor;
    Border borderColor;
    PairComponent(String labelText){
        labelWidth=100;
        textFieldWidth=120;
        labelBackColor=new Color(15,15,15);
        labelForeColor=new Color(10,200,146);
        tfBackColor=labelBackColor;
        tfForeColor=new Color(225,225,225);
        borderColor=BorderFactory.createLineBorder(new Color(46,46,46),1);

        if(currentXPos+labelWidth+textFieldWidth>MyFrame.frameWidth){
            prevXPos=currentXPos;
            prevYPos=currentYPos;
            currentXPos=5;
            currentYPos=currentYPos+fontSize+5+heightSpacing;
        }
        
        label=new JLabel(labelText);
        label.setOpaque(true);
        label.setBounds(currentXPos,currentYPos,labelWidth,fontSize+5);
        label.setBackground(labelBackColor);
        label.setForeground(labelForeColor);
        label.setFont(new Font("Times New Roman",Font.PLAIN,fontSize));
        label.setBorder(borderColor);
        
        textField=new JTextField();
        textField.setOpaque(true);
        textField.setBounds(currentXPos+labelWidth,currentYPos,textFieldWidth,fontSize+5);
        textField.setBackground(tfBackColor);
        textField.setForeground(tfForeColor);
        textField.setFont(new Font("Times New Roman",Font.PLAIN,fontSize));
        textField.setBorder(borderColor);

        prevXPos=currentXPos;
        prevYPos=currentYPos;
        currentXPos=currentXPos+labelWidth+textFieldWidth+widthSpacing;
    }
    void setLabelWidth(int width){
        if(prevXPos+width+textFieldWidth>MyFrame.frameWidth){
            prevXPos=5;
            prevYPos=prevYPos+fontSize+5+heightSpacing;
            currentYPos=prevYPos;
        }
        label.setBounds(prevXPos,prevYPos,width,fontSize+5);
        textField.setBounds(prevXPos+width,prevYPos,textFieldWidth,fontSize+5);
        currentXPos=prevXPos+width+textFieldWidth+widthSpacing;
        labelWidth=width;
    }
    void setTextFieldWidth(int width){
        if(prevXPos+labelWidth+width>MyFrame.frameWidth){
            prevXPos=5;
            prevYPos=prevYPos+fontSize+5+heightSpacing;
            currentYPos=prevYPos;
        }
        label.setBounds(prevXPos,prevYPos,labelWidth,fontSize+5);
        textField.setBounds(prevXPos+labelWidth,prevYPos,width,fontSize+5);
        currentXPos=prevXPos+labelWidth+width+widthSpacing;
        textFieldWidth=width;
    }
    void adjustWidthOnText(String str){
        setLabelWidth(str.length()*15+20);
    }
}
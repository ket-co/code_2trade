import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
class MyFrame extends JFrame{
    static int frameWidth,frameHeight;
    boolean isWatchListed=false;
    int fontSize=30;
    JLabel topLabel;
    JButton backButton,exitButton,watchListStatus;
    Color compBackColor,compForeColor;
    Border borderColor;
    MyFrame(){
        compBackColor=new Color(27,27,27);
        compForeColor=new Color(10,255,146);
        borderColor=BorderFactory.createLineBorder(new Color(46,46,46),1);
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        getContentPane().setBackground(new Color(18,18,18));
    }
    void calcMeasure(){
        frameWidth=getWidth();
        frameHeight=getHeight();
    }
    void addTopLabel(String str){
        topLabel=new JLabel(str);
        topLabel.setBounds(0,0,frameWidth,fontSize+5);
        topLabel.setOpaque(true);
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setBackground(compBackColor);
        topLabel.setForeground(compForeColor);
        topLabel.setFont(new Font("Times New Roman",Font.BOLD,fontSize));
        topLabel.setBorder(borderColor);
        add(topLabel);
    }
    void addBackButton(){
        backButton=new JButton("Back");
        backButton.setBounds(0,frameHeight-fontSize-5,100,fontSize+5);
        backButton.setBackground(compBackColor);
        backButton.setForeground(compForeColor);
        backButton.setFont(new Font("Times New Roman",Font.BOLD,fontSize));
        backButton.setBorder(borderColor);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                StockScreener ssobj=new StockScreener();
                PairComponent.currentXPos=5;
                PairComponent.currentYPos=150;
                PairComponent.prevXPos=5;
                PairComponent.prevYPos=150;
                PairComponent.errorCounter=0;
                ssobj.MainFrame();
                dispose();
            }
        });
        add(backButton);
    }
    void addExitButton(){
        exitButton=new JButton("Exit");
        exitButton.setBounds(frameWidth-100,frameHeight-fontSize-5,100,fontSize+5);
        exitButton.setBackground(compBackColor);
        exitButton.setForeground(compForeColor);
        exitButton.setFont(new Font("Times New Roman",Font.BOLD,fontSize));
        exitButton.setBorder(borderColor);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        add(exitButton);
    }
    void addWatchListStatusButton(){
        watchListStatus=new JButton("WatchList");
        watchListStatus.setBounds(frameWidth-150,fontSize+5,150,fontSize+5);
        watchListStatus.setBackground(compBackColor);
        watchListStatus.setForeground(Color.DARK_GRAY);
        watchListStatus.setFont(new Font("Times New Roman",Font.BOLD,fontSize));
        watchListStatus.setBorder(borderColor);
        watchListStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(isWatchListed){
                    watchListStatus.setForeground(Color.DARK_GRAY);
                    isWatchListed=false;
                }else{
                    watchListStatus.setForeground(compForeColor);
                    isWatchListed=true;
                }
            }
        });
        add(watchListStatus);
    }
    void addPairComponent(JLabel l,JTextField tf){
        add(l);
        add(tf);
    }
}
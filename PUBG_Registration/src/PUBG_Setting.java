import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Scrollbar;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import javax.swing.JOptionPane;
class PUBG_Setting{
   Frame scrollbarFrame = new Frame();
   Frame mainFrame = new Frame();
   Frame subRegisterFrame = new Frame();
   Frame subPointsFrame = new Frame();
   Frame subResultFrame = new Frame();
   Scrollbar[] scrollbarArray = new Scrollbar[3];
   Label costomLabel = new Label("Select Your Costom Theme");
   Button mainFrameRegisterButton = new Button("Register");
   Button mainFramePointsButton = new Button("Points");
   Button mainFrameResultButton = new Button("Result");
   Button[] subRegisterFrameArray = new Button[4];
   Button[] subPointsFrameArray = new Button[3];
   Button[] subResultFrameArray = new Button[5];
   Button scrollbarFrameApplyButton = new Button("Apply");
   PUBG p = new PUBG();
   PUBGResult pr = new PUBGResult();
   String[] subRegisterFrameString = new String[]{"Top Label", "Text Field", "Register Complete", "Done"};
   String[] subPointsFrameString = new String[]{"Points", "Total Kills", "Result"};
   String[] subResultFrameString = new String[]{"First", "Second", "Third", "Others", "ResetAll"};
   String[] subRegisterFrameStringID = new String[]{"TopLabel", "TextField", "RegisterComplete", "Done"};
   String[] subPointsFrameStringID = new String[]{"Points", "TotalKills", "Result"};
   String currentStringID = "";
   String[] colorStringArray = new String[12];
   public void MainFrame() {
      GetRGBToStringFromFile();
      SetRGBToFileFromString();
      mainFrame.setExtendedState(6);
      mainFrame.setLayout((LayoutManager)null);
      mainFrame.setVisible(true);
      mainFrame.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent e) {
            mainFrame.dispose();
            p.f.dispose();
            subRegisterFrame.dispose();
            subPointsFrame.dispose();
            scrollbarFrame.dispose();
            SetRGBToFileFromString();
         }
      });
      costomLabel.setBounds(450, 200, 700, 60);
      costomLabel.setBackground(Color.CYAN);
      costomLabel.setFont(new Font("", Font.BOLD, 50));
      costomLabel.setAlignment(1);
      mainFrame.add(costomLabel);
      mainFrameRegisterButton.setBounds(450, 300, 700, 60);
      mainFrameRegisterButton.setBackground(Color.CYAN);
      mainFrameRegisterButton.setFont(new Font("", Font.BOLD, 50));
      mainFrame.add(mainFrameRegisterButton);
      mainFramePointsButton.setBounds(450, 400, 700, 60);
      mainFramePointsButton.setBackground(Color.CYAN);
      mainFramePointsButton.setFont(new Font("", Font.BOLD, 50));
      mainFrame.add(mainFramePointsButton);
      mainFrameResultButton.setBounds(450, 500, 700, 60);
      mainFrameResultButton.setBackground(Color.CYAN);
      mainFrameResultButton.setFont(new Font("", Font.BOLD, 50));
      mainFrame.add(mainFrameResultButton);
      mainFrameRegisterButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            RegisterFrame();
         }
      });
      mainFramePointsButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            PointsFrame();
         }
      });
      mainFrameResultButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ResultFrame();
         }
      });
   }

   public void GetRGBToStringFromFile() {
      try {
         File file = new File("Color.txt");
         Scanner sc = new Scanner(file);
         for(int i = 0; i < colorStringArray.length; ++i) {
            colorStringArray[i] = sc.nextLine();
         }
         sc.close();
      } catch (Exception e) {
      }

   }
   public void SetRGBToString(int R, int G, int B) {
      if (currentStringID == "TopLabel") {
         colorStringArray[0] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "TextField") {
         colorStringArray[1] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "RegisterComplete") {
         colorStringArray[2] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "Done") {
         colorStringArray[3] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "Points") {
         colorStringArray[4] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "TotalKills") {
         colorStringArray[5] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "Result") {
         colorStringArray[6] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "First") {
         colorStringArray[7] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "Second") {
         colorStringArray[8] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "Third") {
         colorStringArray[9] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "Others") {
         colorStringArray[10] = currentStringID + ": " + R + " " + G + " " + B;
      } else if (currentStringID == "ResetAll") {
         colorStringArray[11] = currentStringID + ": " + R + " " + G + " " + B;
      }

   }
   public void SetRGBToFileFromString() {
      try {
         FileWriter file = new FileWriter("Color.txt");

         for(int i = 0; i < colorStringArray.length; ++i) {
            file.write(colorStringArray[i]+ "\n");
         }
         file.close();
      } catch (Exception e) {
      }

   }
   public void RegisterFrame() {
      p.CommonFrame();
      p.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            p.f.dispose();
            subRegisterFrame.dispose();
            scrollbarFrame.dispose();
            mainFrame.dispose();
            SetRGBToFileFromString();
         }
      });
      p.topLabel.setText("Register");
      p.registerComplete.setBounds(9 * p.size, p.size / 3 + 10, 5 * p.size, p.size / 2);
      p.registerComplete.setFont(new Font("", Font.BOLD, p.size / 2));
      p.registerComplete.setBackground(p.colorArray[2]);
      p.f.add(p.registerComplete);
      p.teamNameLabel.setText("Team Name");
      p.teamNameTextField.setText("Team Name 1");
      p.teamNameTextField.setEditable(false);

      for(int i = 0; i < 4; ++i) {
         p.playerNameTextField[i].setText("Player " + (i + 1));
         p.playerNameTextField[i].setEditable(false);
      }
      SubRegisterFrame();
   }
   public void SubRegisterFrame() {
      subRegisterFrame.setSize(500, 500);
      subRegisterFrame.setLayout(null);
      subRegisterFrame.setVisible(true);
      subRegisterFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            JOptionPane.showMessageDialog(null,"You Can Minimize The Frame");
         }
      });
      int i;
      for(i = 0; i < 4; ++i) {
         subRegisterFrameArray[i] = new Button(subRegisterFrameString[i]);
         subRegisterFrameArray[i].setFont(new Font("", Font.PLAIN, 40));
         subRegisterFrameArray[i].setBounds(50, i * 100 + 50, 400, 50);
         subRegisterFrameArray[i].setBackground(Color.cyan);
         subRegisterFrame.add(subRegisterFrameArray[i]);
      }
      for(i = 0; i < 4; ++i) {
         int valN=i;
         subRegisterFrameArray[i].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               currentStringID = subRegisterFrameStringID[valN];
            }
         });
      }
      ScrollBarFrame();
   }
   public void ScrollBarFrame() {
      scrollbarFrame.setSize(500, 500);
      scrollbarFrame.setLayout(null);
      scrollbarFrame.setVisible(true);
      scrollbarFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            JOptionPane.showMessageDialog(null,"You Can Minimize The Frame");
         }
      });
      int i;
      for(i = 0; i < 3; ++i) {
         scrollbarArray[i] = new Scrollbar(Scrollbar.VERTICAL, 0, 1, 0, 256);
         scrollbarArray[i].setBounds(i * 100 + 50, 30, 50, 450);
         scrollbarFrame.add(scrollbarArray[i]);
      }
      scrollbarArray[0].setBackground(Color.RED);
      scrollbarArray[1].setBackground(Color.GREEN);
      scrollbarArray[2].setBackground(Color.BLUE);
      for(i = 0; i < 3; ++i) {
         scrollbarArray[i].addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
               ColorForCurrentID(scrollbarArray[0].getValue(), scrollbarArray[1].getValue(), scrollbarArray[2].getValue());
            }
         });
      }
      scrollbarFrameApplyButton.setFont(new Font("", Font.PLAIN, 40));
      scrollbarFrameApplyButton.setBounds(340, 400, 120, 50);
      scrollbarFrameApplyButton.setBackground(Color.cyan);
      scrollbarFrame.add(scrollbarFrameApplyButton);
      scrollbarFrameApplyButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SetRGBToString(scrollbarArray[0].getValue(), scrollbarArray[1].getValue(), scrollbarArray[2].getValue());
         }
      });
   }
   public void ColorForCurrentID(int R, int G, int B) {
      Color colorForID = new Color(R, G, B);
      if (currentStringID == "TopLabel") {
         p.topLabel.setBackground(colorForID);
      } else {
         int i;
         if (currentStringID == "TextField") {
            p.teamNameLabel.setBackground(colorForID);
            p.teamNameTextField.setBackground(colorForID);

            for(i = 0; i < 4; ++i) {
               p.playerNameTextField[i].setBackground(colorForID);
            }
         } else if (currentStringID == "RegisterComplete") {
            p.registerComplete.setBackground(colorForID);
         } else if (currentStringID == "Done") {
            p.done.setBackground(colorForID);
         } else if (currentStringID == "Points") {
            p.pointsLabel.setBackground(colorForID);
         } else if (currentStringID == "TotalKills") {
            p.killsLabel.setBackground(colorForID);
            p.killsTextField.setBackground(colorForID);
         } else if (currentStringID == "Result") {
            p.resultButton.setBackground(colorForID);
         } else if (currentStringID == "First") {
            for(i = 0; i < 6; ++i) {
               pr.resultLabel[0][i].setBackground(colorForID);
            }
         } else if (currentStringID == "Second") {
            for(i = 0; i < 6; ++i) {
               pr.resultLabel[1][i].setBackground(colorForID);
            }
         } else if (currentStringID == "Third") {
            for(i = 0; i < 6; ++i) {
               pr.resultLabel[2][i].setBackground(colorForID);
            }
         } else if (currentStringID == "Others") {
            for(i = 3; i < 20; ++i) {
               for(int j = 0; j < 6; ++j) {
                  pr.resultLabel[i][j].setBackground(colorForID);
               }
            }
         } else if (currentStringID == "ResetAll") {
            pr.resetAll.setBackground(colorForID);
         }
      }

   }
   public void PointsFrame() {
      p.CommonFrame();
      p.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            p.f.dispose();
            subPointsFrame.dispose();
            scrollbarFrame.dispose();
            mainFrame.dispose();
            SetRGBToFileFromString();
         }
      });
      p.teamNameLabel.setBounds(5 * p.size + p.size / 2, 2 * p.size, 4 * p.size, p.size / 2);
      p.teamNameTextField.setBounds(2 * p.size - p.size / 2, 2 * p.size, p.size * 3 + p.size / 2, p.size / 2);
      p.pointsLabel.setBounds(10 * p.size + p.size / 2, 2 * p.size, 2 * p.size + 20, p.size / 2);
      p.pointsLabel.setFont(new Font("", Font.PLAIN, p.size / 2));
      p.pointsLabel.setBackground(p.colorArray[4]);
      p.f.add(p.pointsLabel);
      p.killsLabel.setBounds(10 * p.size + p.size / 2, 3 * p.size, 2 * p.size + p.size / 2 - 10, p.size / 2);
      p.killsLabel.setFont(new Font("", Font.PLAIN, p.size / 2));
      p.killsLabel.setBackground(p.colorArray[5]);
      p.f.add(p.killsLabel);
      p.killsTextField.setBounds(10 * p.size + p.size / 2, p.size + 3 * p.size, 2 * p.size, p.size / 2 + 10);
      p.killsTextField.setFont(new Font("", Font.PLAIN, p.size / 2));
      p.killsTextField.setBackground(p.colorArray[5]);
      p.f.add(p.killsTextField);
      p.resultButton.setBounds(10 * p.size + p.size / 2, p.size, 2 * p.size + 20, p.size / 2);
      p.resultButton.setFont(new Font("", Font.PLAIN, p.size / 2));
      p.resultButton.setBackground(p.colorArray[6]);
      p.f.add(p.resultButton);
      p.topLabel.setText("Round 1");
      p.teamNameLabel.setText("Team Name");
      p.teamNameTextField.setText("Search Team");
      p.teamNameTextField.setEditable(false);
      for(int i = 0; i < 4; ++i) {
         p.playerNameTextField[i].setText("Player " + (i + 1));
         p.playerNameTextField[i].setEditable(false);
      }
      p.pointsLabel.setText("Points:15");
      p.killsTextField.setText("4");
      SubPointsFrame();
   }
   public void SubPointsFrame() {
      subPointsFrame.setSize(500, 500);
      subPointsFrame.setLayout(null);  
      subPointsFrame.setVisible(true);
      subPointsFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            JOptionPane.showMessageDialog(null,"You Can Minimize The Frame");
         }
      });
      int i;
      for(i = 0; i < 3; ++i) {
         subPointsFrameArray[i] = new Button(subPointsFrameString[i]);
         subPointsFrameArray[i].setFont(new Font("", Font.PLAIN, 40));
         subPointsFrameArray[i].setBounds(50, i * 100 + 50, 400, 50);
         subPointsFrameArray[i].setBackground(Color.cyan);
         subPointsFrame.add(subPointsFrameArray[i]);
      }
      for(i = 0; i < 3; ++i) {
         int valN=i;
         subPointsFrameArray[i].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               currentStringID = subPointsFrameStringID[valN];
            }
         });
      }
      ScrollBarFrame();
   }
   public void ResultFrame() {
      pr.bool = false;
      pr.resultFrame.setExtendedState(6);
      pr.resultFrame.setLayout(null);
      pr.resultFrame.setVisible(true);
      pr.resultFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            pr.resultFrame.dispose();
            subResultFrame.dispose();
            scrollbarFrame.dispose();
            mainFrame.dispose();
            SetRGBToFileFromString();
   }
      });
      pr.ResultFrame();
      pr.resultFrame.remove(pr.backButton);
      pr.resetAll.setBounds(12 * pr.size + pr.size / 2, 7 * pr.size + pr.size / 2, 2 * pr.size + 20, pr.size / 2);
      pr.resetAll.setFont(new Font("", Font.PLAIN, pr.size / 2));
      pr.resetAll.setBackground(pr.colorArray[4]);
      pr.resultFrame.add(pr.resetAll);
      for(int i = 0; i < pr.teamNumber; ++i) {
         for(int j = 0; j < 6; ++j) {
            if (j == 0) {
               pr.resultLabel[i][j].setText(i + 1 + ". Points:N");
            } else if (j == 1) {
               pr.resultLabel[i][j].setText("Team " + (i + 1) + ":");
            } else {
               pr.resultLabel[i][j].setText("Player " + (j + 1));
            }
         }
      }
      SubResultFrame();
   }
   public void SubResultFrame() {
      subResultFrame.setSize(500, 550);
      subResultFrame.setLayout(null);
      subResultFrame.setVisible(true);
      subResultFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            JOptionPane.showMessageDialog(null,"You Can Minimize The Frame");
         }
      });
      int i;
      for(i = 0; i < 5; ++i) {
         subResultFrameArray[i] = new Button(subResultFrameString[i]);
         subResultFrameArray[i].setFont(new Font("", Font.PLAIN, 40));
         subResultFrameArray[i].setBounds(50, i * 100 + 50, 400, 50);
         subResultFrameArray[i].setBackground(Color.cyan);
         subResultFrame.add(subResultFrameArray[i]);
      }
      for(i = 0; i < 5; ++i) {
         int valN=i;
         subResultFrameArray[i].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               currentStringID = subResultFrameString[valN];
            }
         });
      }
      ScrollBarFrame();
   }
   public static void main(String args[]) {
      PUBG_Setting pubg_Setting = new PUBG_Setting();
      pubg_Setting.MainFrame();
   }
}
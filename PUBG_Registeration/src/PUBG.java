import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.TextEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JOptionPane;
class PUBG{
   int round;
   int teamNameCount;
   int size;
   int teamNumber = -1;
   int[] stack;
   int[] valueOfPoints;
   int currentTeamPoints = 1;
   int tempTeamNumber = -1;
   int points = 0;
   Image backgroundImage = Toolkit.getDefaultToolkit().getImage("Background.jpg");
   Frame f = new Frame(){
      public void paint(Graphics g) {
         g.drawImage(backgroundImage, 0, 0,getWidth(),getHeight(),this);
      }
   };
   Label topLabel = new Label();
   Label teamNameLabel = new Label();
   Label[] playerNameLabel = new Label[4];
   TextField teamNameTextField = new TextField();
   TextField[] playerNameTextField = new TextField[4];
   Button done = new Button("Done");
   Button exitButton = new Button("Exit");
   Button registerComplete = new Button("Register Complete");
   Button resultButton = new Button("Result");
   String teamNameString;
   String[] playerNameString = new String[4];
   String[][] teamNameArray;
   Color[] colorArray = new Color[7];
   Color[] colorTeamNumber = new Color[4];
   Label pointsLabel = new Label();
   Label killsLabel = new Label("Total Kills:");
   TextField killsTextField = new TextField();
   Label markLabel=new Label("Made By Code2Trade");
   PUBG() {
      Round();
      TeamCount();
      colorTeamNumber[0] = Color.YELLOW;
      colorTeamNumber[1] = Color.ORANGE;
      colorTeamNumber[2] = Color.BLUE;
      colorTeamNumber[3] = Color.GREEN;
      GetColor();
   }
   public void GetColor() {
      try {
         File file = new File("Color.txt");
         Scanner sc = new Scanner(file);

         for(int i = 0; sc.hasNextLine(); ++i) {
            String str = sc.next();
            int R = sc.nextInt();
            int G = sc.nextInt();
            int B = sc.nextInt();
            colorArray[i] = new Color(R, G, B);
         }
         sc.close();
      } catch (Exception e) {
      }

   }

   public void Round() {
      File file = new File("Round.txt");

      try {
         Scanner sc = new Scanner(file);
         round = sc.nextInt();
         sc.close();
      } catch (Exception e) {
      }

   }

   public void TeamCount() {
      File file = new File("TeamCount.txt");

      try {
         Scanner sc = new Scanner(file);
         teamNameCount = sc.nextInt();
         sc.close();
      } catch (Exception e) {
      }

   }
   public String CreateTeamName() {
      for(int i = 0; i < 4; ++i) {
         if (!playerNameTextField[i].getText().isEmpty()) {
            TextField pT = playerNameTextField[i];
            return pT.getText() + "'s Team";
         }
      }

      return "unknown";
   }
   public void UpdateTeamName() {
      try {
         FileWriter file = new FileWriter("TeamCount.txt");
         file.write(String.valueOf(++teamNameCount));
         file.close();
      } catch (Exception e) {
      }
   }
   public void RegisterNameToFile() {
      try {
         FileWriter file = new FileWriter("TeamName.txt", true);
         int teamNo = teamNameCount - 1;
         file.append("Team Name " + teamNo + ":" + teamNameString + ":\n");
         for(int i = 0; i < 4; ++i) {
            file.append("Player " + (i + 1) + ":" + playerNameString[i] + "\n");
         }
         file.close();
      } catch (Exception e) {
      }
   }
   public void CommonFrame(){
      f.setExtendedState(6);
      f.setLayout(null);
      f.setUndecorated(true);
      f.setVisible(true);
      size = f.getWidth() / 15;
      topLabel.setBounds(size * 6 + size / 2, size / 3 + 10, 2 * size + 10, size / 2 + 10);
      topLabel.setFont(new Font("", Font.BOLD, size / 2));
      topLabel.setBackground(colorArray[0]);
      f.add(topLabel);
      teamNameLabel.setBounds(2 * size - size / 2, 2 * size, size * 3 + size / 2, size / 2);
      teamNameLabel.setFont(new Font("", Font.PLAIN, size / 2));
      teamNameLabel.setBackground(colorArray[1]);
      f.add(teamNameLabel);
      int i;
      for(i = 0; i < 4; ++i) {
         playerNameLabel[i] = new Label(String.valueOf(i + 1));
         playerNameLabel[i].setBounds(4 * size + size / 2, i * size + 3 * size, size / 2, size / 2);
         playerNameLabel[i].setFont(new Font("", 0, size / 2));
         playerNameLabel[i].setAlignment(Label.CENTER);
         playerNameLabel[i].setBackground(colorTeamNumber[i]);
         f.add(playerNameLabel[i]);
      }
      teamNameTextField.setBounds(5 * size + size / 2, 2 * size, 4 * size, size / 2);
      teamNameTextField.setFont(new Font("", Font.PLAIN, size / 2 - 10));
      teamNameTextField.setBackground(colorArray[1]);
      f.add(teamNameTextField);
      for(i = 0; i < 4; ++i) {
         playerNameTextField[i] = new TextField();
         playerNameTextField[i].setBounds(5 * size + size / 2, i * size + 3 * size, 4 * size, size / 2);
         playerNameTextField[i].setFont(new Font("", Font.PLAIN, size / 2 - 10));
         playerNameTextField[i].setBackground(colorArray[1]);
         if (i < 3) {
            int currentIndex=i;
            playerNameTextField[i].addActionListener((ActionListener) new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  playerNameTextField[currentIndex + 1].requestFocus();
               }
            });
         }
         f.add(playerNameTextField[i]);
      }
      done.setBounds(10 * size + size / 2, 3 * size + 3 * size, 2 * size, size / 2);
      done.setFont(new Font("", Font.PLAIN, size / 2));
      done.setBackground(colorArray[3]);
      f.add(done);
      exitButton.setBounds(10 * size + size / 2, 4 * size + 3 * size, 2 * size, size / 2);
      exitButton.setFont(new Font("", Font.PLAIN, size / 2));
      exitButton.setBackground(Color.GREEN);
      f.add(exitButton);
      markLabel.setBounds(12 * size + size+5, 4 * size + 4 * size+20, 2 * size, size / 3);
      markLabel.setFont(new Font("", Font.BOLD, size / 5));
      markLabel.setBackground(Color.BLACK);
      markLabel.setForeground(Color.WHITE);
      f.add(markLabel);
   }
   public void RegisterComplete() {
      try {
         FileWriter file = new FileWriter("Round.txt");
         file.write(String.valueOf(round + 1));
         file.close();
      } catch (Exception e) {
      }

   }
   public void Register() {
      topLabel.setText("Register");
      teamNameLabel.setText("Team Name " + String.valueOf(teamNameCount));
      teamNameTextField.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            playerNameTextField[0].requestFocus();
         }
      });
      done.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         boolean bool = true;
         int i;
         for(i = 0; i < 4; ++i) {
            bool &=playerNameTextField[i].getText().isEmpty();
         }
         if (!bool) {
            if (teamNameCount > 19) {
               f.dispose();
            }
            teamNameString = teamNameTextField.getText();
            if (teamNameString.isEmpty()) {
               teamNameString = CreateTeamName();
            }
            teamNameTextField.setText((String)null);
            UpdateTeamName();
            teamNameLabel.setText("Team Name " + String.valueOf(teamNameCount));
            for(i = 0; i < 4; ++i) {
               playerNameString[i] = playerNameTextField[i].getText();
               if (playerNameString[i].isEmpty()) {
                  playerNameString[i] = "No";
               }
               playerNameTextField[i].setText((String)null);
            }
            RegisterNameToFile();
            teamNameTextField.requestFocus();
         }

      }
      });
      CommonFrame();
      f.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            f.dispose();
         }
      });
      exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            f.dispose();
         }
      });
      registerComplete.setBounds(9 * size, size / 3 + 10, 5 * size, size / 2);
      registerComplete.setFont(new Font("", Font.BOLD, size / 2));
      registerComplete.setBackground(colorArray[2]);
      registerComplete.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean bool = true;
      
            for(int i = 0; i < 4; ++i) {
               bool &=playerNameTextField[i].getText().isEmpty();
            }
            if (!bool || bool && teamNameCount > 1) {
               RegisterComplete();
               f.dispose();
            }
      
         }
      });
      f.add(registerComplete);
   }
   public void GetTeamName() {
      teamNameArray = new String[teamNameCount - 1][5];
      try {
         File file = new File("TeamName.txt");
         Scanner sc = new Scanner(file);
         for(int i = 0; i < teamNameCount - 1; ++i) {
            for(int j = 0; j < 5; ++j) {
               teamNameArray[i][j] = sc.nextLine();
            }
         }
         sc.close();
      } catch (Exception e) {
      }

   }
   public void Result() {
      resultButton.setBounds(10 * size + size / 2, size, 2 * size + 20, size / 2);
      resultButton.setFont(new Font("", Font.PLAIN, size / 2));
      resultButton.setBackground(colorArray[6]);
      f.add(resultButton);
      resultButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            PUBGResult pubgResult = new PUBGResult();
            pubgResult.start();
            pubgResult.GetTeamName();
            pubgResult.Sort();
            pubgResult.Result();
         }
      });
   }
   public boolean foundInStack(int n) {
      for(int i = 0; i < stack.length; ++i) {
         if (stack[i] == n) {
            return true;
         }
      }
      return false;
   }
   public int Search(String str) {
      for(int i = 0; i < this.teamNameCount - 1; ++i) {
         if (!foundInStack(i)) {
            for(int j = 0; j < 5; ++j) {
               if (teamNameArray[i][j].contains(str)) {
                  return i;
               }
            }
         }
      }
      return -1;
   }
   public boolean isInteger(String str) {
      try {
         Integer.parseInt(str);
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   public void SetTeamName() {
      try {
         FileWriter file = new FileWriter("TeamName.txt");

         for(int i = 0; i < this.teamNameCount - 1; ++i) {
            for(int j = 0; j < 5; ++j) {
               file.write(this.teamNameArray[i][j] + "\n");
            }
         }

         file.close();
      } catch (Exception e) {
      }

   }
   public void Point() {
      valueOfPoints = new int[teamNameCount + 1];
      valueOfPoints[0] = 0;
      stack = new int[teamNameCount - 1];
      int i;
      for(i = 0; i < stack.length; ++i) {
         stack[i] = -1;
      }
      GetTeamName();
      CommonFrame();
      exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,"Please Insert The Kills Of All Team's");
         }
      });
      if (round > 1) {
         Result();
      }

      teamNameLabel.setBounds(5 * size + size / 2, 2 * size, 4 * size, size / 2);
      teamNameTextField.setBounds(2 * size - size / 2, 2 * size, size * 3 + size / 2, size / 2);
      teamNameTextField.requestFocus();
      topLabel.setText("Round " + String.valueOf(round));
      pointsLabel.setBounds(10 * size + size / 2, 2 * size, 2 * size + 20, size / 2);
      pointsLabel.setFont(new Font("", Font.PLAIN, size / 2));
      pointsLabel.setBackground(colorArray[4]);
      f.add(pointsLabel);
      killsLabel.setBounds(10 * size + size / 2, 3 * size, 2 * size + size / 2 - 10, size / 2);
      killsLabel.setFont(new Font("", Font.PLAIN, size / 2));
      killsLabel.setBackground(colorArray[5]);
      f.add(killsLabel);
      killsTextField.setBounds(10 * size + size / 2, size + 3 * size, 2 * size, size / 2 + 10);
      killsTextField.setFont(new Font("", Font.PLAIN, size / 2));
      killsTextField.setBackground(colorArray[5]);
      f.add(killsTextField);
      teamNameTextField.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            killsTextField.requestFocus();
         }
      });
      for(i = 0; i < 4; ++i) {
         playerNameTextField[i].setEditable(false);
      }
      for(i = 1; i < valueOfPoints.length; ++i) {
         if (i == 1) {
            valueOfPoints[i] = 15;
         } else if (i == 2) {
            valueOfPoints[i] = 12;
         } else if (i == 3) {
            valueOfPoints[i] = 10;
         } else if (i != 4 && i != 5) {
            if (i > 5 && i < 11) {
               valueOfPoints[i] = 6;
            } else {
               valueOfPoints[i] = 4;
            }
         } else {
            valueOfPoints[i] = 8;
         }
      }
      int valueOfP = valueOfPoints[currentTeamPoints];
      pointsLabel.setText("Points:" + String.valueOf(valueOfP));
      teamNameTextField.addTextListener(new TextListener() {
         public void textValueChanged(TextEvent e) {
            String strTeamName = teamNameTextField.getText();
            if (!strTeamName.isBlank()) {
               tempTeamNumber = Search(strTeamName);
               if (tempTeamNumber != -1) {
                  teamNumber = tempTeamNumber;
                  String teamNames = teamNameArray[teamNumber][0];
                  int indexOfColumn = teamNames.indexOf(":");
                  teamNameLabel.setText(teamNames.substring(indexOfColumn + 1, teamNames.indexOf(":", indexOfColumn + 1)));
      
                  for(int i = 0; i < 4; ++i) {
                     playerNameTextField[i].setText(teamNameArray[teamNumber][i + 1].substring(teamNameArray[teamNumber][i + 1].indexOf(":") + 1));
                  }
               }
            }
      
         }
      });
      done.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean bool = true;
            int i;
            for(i = 0; i < 4; ++i) {
               bool &= playerNameTextField[i].getText().isEmpty();
            }
            if (!bool && !killsTextField.getText().isBlank() && isInteger(killsTextField.getText())) {
               points = Integer.parseInt(killsTextField.getText());
               stack[currentTeamPoints - 1] = teamNumber;
               String[] strPlayerNames = teamNameArray[teamNumber];
               String strTeamNames = teamNameArray[teamNumber][0];
               strPlayerNames[0] = strTeamNames + "Point" + String.valueOf(round) + ":" + String.valueOf(points + valueOfPoints[currentTeamPoints]) + ":";
               teamNameTextField.setText((String)null);
               teamNameLabel.setText((String)null);
      
               for(i = 0; i < 4; ++i) {
                  playerNameTextField[i].setText((String)null);
               }
      
               killsTextField.setText((String)null);
               teamNameTextField.requestFocus();
               ++currentTeamPoints;
               int vOfP = valueOfPoints[currentTeamPoints];
               pointsLabel.setText("Points:" + String.valueOf(vOfP));
               if (currentTeamPoints >= teamNameCount) {
                  f.dispose();
                  SetTeamName();
                  RegisterComplete();
               }
            }
      
         }
      });
   }
   public void FinalResult() {
      PUBGResult pubgResult = new PUBGResult();
      pubgResult.start();
      pubgResult.GetTeamName();
      pubgResult.Sort();
      pubgResult.FinalResult();
   }
   public static void main(String[] args) {
      PUBG pubg = new PUBG();
      if (pubg.round == 0) {
         pubg.Register();
      } else if (pubg.round > 0 && pubg.round < 4) {
         pubg.Point();
      } else {
         pubg.FinalResult();
      }
   }
}
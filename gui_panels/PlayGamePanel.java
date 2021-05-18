package gui_panels;

import gui_components.AnswerButtons;
import question.Answer;
import question.Question;
import life_lines.PhoneAFriend;
import life_lines.AskTheAudience;
import life_lines.AbstractPlayerGameHelp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import animation.GameState;
import driver.PlayGame;
import gui_components.WalkAwayButton;
import java.util.Observable;
import java.util.Observer;
import player.Player;
import question.QuestionTimer;

/**
 *
 * @author Rhys Van Rooyen, Student ID: 19049569
 */
public class PlayGamePanel extends JPanel implements Observer {

    private WalkAwayButton walkAwayButton;
    private LifeLinePanel lifeLinesPanel;
    private AnswerButtons answersPanel;
    private JLabel questionLabel;
    private JLabel timerLabel;
    private final int INSIDE_PADDING = 20;
    private int panelWidth = 1280;
    private int panelHeight = 720;
    private final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    private final GameState gameState;
    private ArrayList<Question> questions;
    private int currentQuestion;
    private final Color INTIAL_TIMER_COLOR = new Color(63, 255, 202);

    public PlayGamePanel(int panelWidth, int panelHeight, GameState gameState) {
        this.gameState = gameState;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        questions = PlayGame.getNewQuestions();
        currentQuestion = 0;

        // Initialise attributes for panels
        walkAwayButton = new WalkAwayButton("Walk Away", new Dimension(100, 60), gameState);
        lifeLinesPanel = new LifeLinePanel(new Dimension(380, 60), gameState, new Color(64, 64, 206), new Color(64, 206, 135), BACKGROUND_COLOR, BACKGROUND_COLOR);
        answersPanel = new AnswerButtons(new Dimension((this.panelWidth - (2 * INSIDE_PADDING)), 320),
                BACKGROUND_COLOR, new Color(64, 206, 135), new Color(255, 255, 255), INSIDE_PADDING, questions.get(currentQuestion).getAnswers());

        // Setting the anonymous method for the answer questionButtons to update
        JButton[] questionButtons = answersPanel.getButtons();
        for (int i = 0; i < questionButtons.length; i++) {
            questionButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameState.setLifeLineUsedThisRound(false);

                    if (!(answersPanel.getCorrectButton() == e.getSource())) { // exit the game
                        resetPanel();
                        gameState.updateRecords();
                        gameState.goToMainMenu();
                    } else { // increment score and change the question
                        answersPanel.setAnswers(questions.get(++currentQuestion).getAnswers());
                        questionLabel.setText(questions.get(currentQuestion).getText());
                        questionTimer.resetCounter();
                        timerLabel.setText(questionTimer.getCounter().toString());
                        timerLabel.setForeground(INTIAL_TIMER_COLOR);

                        Player p = gameState.getPlayer();
                        p.setHighscore(p.getCurrentHighscore() + 1);
                    }
                }
            });
        }

        // bind action listeners to lifelines
        setLifeLineListeners();

        // Creating the question label
        questionLabel = new JLabel(questions.get(0).getText());
        questionLabel.setSize(new Dimension(1000, 70));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setFont(new Font("", Font.BOLD, 26));

        // Creating the timer label
        timerLabel = new JLabel("60");
        timerLabel.setForeground(INTIAL_TIMER_COLOR);
        timerLabel.setSize(100, 60);
        timerLabel.setFont(new Font("", Font.BOLD, 60));

        // Adding walk away features
        walkAwayButton.getWalkAwayButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPanel();
                gameState.updateRecords();
                gameState.setLifeLineUsedThisRound(false);
                gameState.goToMainMenu();
            }
        });

        // Set the requirements for the frame
        super.setSize(new Dimension(this.panelWidth, this.panelHeight));
        super.setLayout(null);

        // Set locations
        walkAwayButton.setLocation(new Point(INSIDE_PADDING, INSIDE_PADDING));
        lifeLinesPanel.setLocation(new Point(this.panelWidth - (380 + INSIDE_PADDING), INSIDE_PADDING));
        answersPanel.setLocation(new Point(INSIDE_PADDING, 320));
        questionLabel.setLocation(new Point(200, 200));
        timerLabel.setLocation(new Point((2 * INSIDE_PADDING) + 100, INSIDE_PADDING));

        // Add relevant J components
        super.setBackground(BACKGROUND_COLOR);
        super.add(walkAwayButton);
        super.add(lifeLinesPanel);
        super.add(answersPanel);
        super.add(questionLabel);
        super.add(timerLabel);
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (questionTimer.getCounter() > 0) {
//            questionTimer.decrementCounter();
//            switch (questionTimer.getCounter()) {
//                case 40:
//                    timerLabel.setForeground(new Color(220, 255, 0));
//                    break;
//                case 20:
//                    timerLabel.setForeground(new Color(255, 169, 0));
//                    break;
//                case 10:
//                    timerLabel.setForeground(new Color(221, 41, 34));
//                    break;
//            }
//
//            timerLabel.setText(questionTimer.getCounter().toString());
//        }
//
//        if (questionTimer.getCounter() <= 0) {
//            questionTimer.stopTimer();
//            questionTimer.resetCounter();
//            timerLabel.setForeground(INTIAL_TIMER_COLOR);
//            timerLabel.setText(questionTimer.getCounter().toString());
//
//            gameState.updateRecords();
//            gameState.setLifeLineUsedThisRound(false);
//            gameState.goToMainMenu();
//        }
//    }

    /**
     * this helper method allows the game to be put back into its initial state
     * with new questions supplied
     */
//    private void resetPanel() {
//        //Reset underlying values
//        questions = PlayGame.resetGame(questionTimer, lifeLinesPanel);
//
//        //Reset timer styling
//        timerLabel.setForeground(INTIAL_TIMER_COLOR);
//        timerLabel.setText(questionTimer.getCounter().toString());
//
//        //Reset question styling
//        currentQuestion = 0;
//        questionLabel.setText(questions.get(currentQuestion).getText());
//        answersPanel.setAnswers(questions.get(currentQuestion).getAnswers());
//
//        // Reset life lines styling
//        lifeLinesPanel.resetLifeLineStyling();
//    }

    private void setLifeLineListeners() {
        lifeLinesPanel.getFiftyFiftyHelper().getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!gameState.isLifeLineUsedThisRound()) {
                    AbstractPlayerGameHelp lifeLine = lifeLinesPanel.getFiftyFiftyHelper();
                    lifeLinesPanel.buttonClicked(lifeLine);

                    if (!lifeLine.isUsed()) {
                        gameState.setLifeLineUsedThisRound(true);
                        setButtonTextBlank(lifeLine.getHelp(questions.get(currentQuestion)));
                        lifeLine.setIsUsed(true);
                    }
                }
            }
        });

        lifeLinesPanel.getAskTheAudienceHelper().getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!gameState.isLifeLineUsedThisRound()) {
                    AskTheAudience lifeLine = lifeLinesPanel.getAskTheAudienceHelper();
                    lifeLinesPanel.buttonClicked(lifeLine);

                    if (!lifeLine.isUsed()) {
                        gameState.setLifeLineUsedThisRound(true);
                        questionLabel.setText("<html>" + questionLabel.getText() + "<br>" + "the two most voted audience options are..." + "</html>");
                        setButtonTextBlank(lifeLine.getHelp(questions.get(currentQuestion)));
                        lifeLine.setIsUsed(true);
                    }
                }
            }
        });

        lifeLinesPanel.getPhoneAFriendHelper().getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!gameState.isLifeLineUsedThisRound()) {
                    PhoneAFriend lifeLine = lifeLinesPanel.getPhoneAFriendHelper();
                    lifeLinesPanel.buttonClicked(lifeLine);

                    if (!lifeLine.isUsed()) {
                        gameState.setLifeLineUsedThisRound(true);
                        ArrayList<Answer> ans = lifeLine.getHelp(questions.get(currentQuestion));
                        questionLabel.setText("<html>" + questionLabel.getText() + "<br>" + lifeLinesPanel.getPhoneAFriendHelper().friendsResponse(ans) + "</html>");
                        setButtonTextBlank(ans);
                        lifeLine.setIsUsed(true);
                    }
                }
            }
        });
    }

    /**
     * This method is used to set the button text of incorrect answers to blank.
     *
     * @param answers, ArrayList of 2 answers
     */
    private void setButtonTextBlank(ArrayList<Answer> answers) {

        // Loop 4 times as only 4 possible answers
        // note that the answers array list only has two possible answers
        for (int i = 0; i < 4; i++) {
            JButton currentAnswer = answersPanel.getButton(i);
            if (!(currentAnswer.getText().equals(answers.get(0).getText())
                    || currentAnswer.getText().equals(answers.get(1).getText()))) {
                currentAnswer.setText("");
            }
        }
    }

    /**
     * Used to get the question timer object
     *
     * @return QuestionTimer, returns the question timer
     */
    public QuestionTimer getCounterTImer() {
        return questionTimer;
    }
    
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof QuestionTimer) {
            QuestionTimer qt = (QuestionTimer) o;

            switch (qt.getCounter()) {
                case 40:
                    timerLabel.setForeground(new Color(220, 255, 0));
                    break;
                case 20:
                    timerLabel.setForeground(new Color(255, 169, 0));
                    break;
                case 10:
                    timerLabel.setForeground(new Color(221, 41, 34));
                    break;
                case 0:
                    timerLabel.setForeground(INTIAL_TIMER_COLOR);
                    timerLabel.setText(qt.getCounter().toString());
                    break;
            }

            timerLabel.setText(qt.getCounter().toString());
        }
        
        
    }
}

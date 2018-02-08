import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;


class ClickAction extends AbstractAction {


    private PuzzleEx puzzleEx;

    public ClickAction(PuzzleEx puzzleEx) {
        this.puzzleEx = puzzleEx;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        checkButton(e);
        puzzleEx.checkSolution();
        puzzleEx.checkRadioBtn();
        puzzleEx.checkSteps();

    }

    private void checkButton(ActionEvent e) {

        int lidx = 0;

        for (MyButton button : puzzleEx.getButtons()) {
            if (button.isLastButton()) {
                lidx = puzzleEx.getButtons().indexOf(button);
            }
        }

        JButton button = (JButton) e.getSource();
        int bidx = puzzleEx.getButtons().indexOf(button);

        if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
            Collections.swap(puzzleEx.getButtons(), bidx, lidx);
            updateButtons();
        }
    }

    private void updateButtons() {

        puzzleEx.getPanel().removeAll();

        for (JComponent btn : puzzleEx.getButtons()) {

            puzzleEx.getPanel().add(btn);
        }

        puzzleEx.getPanel().validate();
    }
}

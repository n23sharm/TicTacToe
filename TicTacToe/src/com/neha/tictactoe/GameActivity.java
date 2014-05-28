package com.neha.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements GameListener {
	public static final String EXTRA_COMPUTER_FIRST_PLAYER = "ComputerFirstPlayer";
	
	private GameManager mGameManager;
	private TextView[][] mCells;
	private Toast mLastToast;
	private boolean mIsGameOver;
	
	private TextView mRematchButton;
	private TextView mStatusText;
	private View mStatusBar;
	private boolean mIsComputerFirst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		initializeView();
		
		mIsComputerFirst = getIntent().getBooleanExtra(EXTRA_COMPUTER_FIRST_PLAYER, true);
		mGameManager = new GameManager(this);
		mGameManager.startGame(mIsComputerFirst);
	}
	
	private void initializeView() {
		mRematchButton = (TextView) findViewById(R.id.rematch_button);
		mStatusText = (TextView) findViewById(R.id.game_status);
		mStatusBar = findViewById(R.id.game_status_bar);
		
		// initializing cells
		mCells = new TextView[3][3];
		mCells[0][0] = (TextView) findViewById(R.id.cell_00);
		mCells[0][1] = (TextView) findViewById(R.id.cell_01);
		mCells[0][2] = (TextView) findViewById(R.id.cell_02);
		mCells[1][0] = (TextView) findViewById(R.id.cell_10);
		mCells[1][1] = (TextView) findViewById(R.id.cell_11);
		mCells[1][2] = (TextView) findViewById(R.id.cell_12);
		mCells[2][0] = (TextView) findViewById(R.id.cell_20);
		mCells[2][1] = (TextView) findViewById(R.id.cell_21);
		mCells[2][2] = (TextView) findViewById(R.id.cell_22);
	}
	
	public void onRematchClicked(View view) {
		mIsGameOver = false;
		mRematchButton.setVisibility(View.GONE);
		mStatusBar.setBackgroundColor(getResources().getColor(R.color.status_bar_default_color));
		mGameManager.startGame(mIsComputerFirst);
	}
	
	public void cellTapped(View view) {
		if (mIsGameOver) {
			displayErrorMessage(getString(R.string.game_is_over));
			return;
		}
		
		String tag = (String) view.getTag();
		int row = Integer.valueOf(tag.substring(0, 1));
		int column = Integer.valueOf(tag.substring(1));
		
		try {
			mGameManager.playHumanMove(row, column);
		} catch (NotYourTurnException e) {
			displayErrorMessage(getString(R.string.error_not_your_turn));
		} catch (InvalidMoveException e) {
			displayErrorMessage(getString(R.string.error_invalid_move));
		}
	}

	private void dismissToast() {
		if (mLastToast != null) {
			mLastToast.cancel();
			mLastToast = null;
		}
	}
	
	private void displayErrorMessage(String error) {
		dismissToast();

		mLastToast = Toast.makeText(this, error, Toast.LENGTH_LONG);
		mLastToast.show();
	}
	
	@Override
	public void onCurrentPlayerChanged(Player currentPlayer) {
		String statusText;
		
		if (currentPlayer == mGameManager.getComputerPlayer()) {
			statusText = getString(R.string.computer_turn);
		} else {
			statusText = getString(R.string.your_turn);
		}

		mStatusText.setText(statusText);
	}
	
	@Override
	public void onBoardUpdated(Player[][] board) {
		dismissToast();
		
		for (int i = 0; i < mCells.length; ++i) {
			for (int j = 0; j < mCells[0].length; ++j) {
				Player boardPlayer = board[i][j];
				int colorResource = boardPlayer == Player.CROSS ? R.color.x_color : R.color.o_color;
				mCells[i][j].setTextColor(getResources().getColor(colorResource));
				mCells[i][j].setText(board[i][j].toString());
			}
		}
	}

	@Override
	public void onGameOver(Player winner) {
		mIsGameOver = true;
		mRematchButton.setVisibility(View.VISIBLE);

		String statusText;
		int backgroundColorResource;
		
		if (winner == mGameManager.getHumanPlayer()) {
			statusText = getString(R.string.you_win);
			backgroundColorResource = R.color.status_bar_win_color;
		} else if (winner == mGameManager.getComputerPlayer()){
			statusText = getString(R.string.you_lost);
			backgroundColorResource = R.color.status_bar_lose_color;
		} else {
			statusText = getString(R.string.tie);
			backgroundColorResource = R.color.status_bar_tie_color;
		}
		
		mStatusText.setText(statusText);
		mStatusBar.setBackgroundColor(getResources().getColor(backgroundColorResource));
	} 
	
	
}

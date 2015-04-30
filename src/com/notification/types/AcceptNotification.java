package com.notification.types;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.theme.WindowTheme;

/**
 * This is a Notification that will ask the user to accept of decline a certain action.
 */
public class AcceptNotification extends TextNotification {
	private JButton m_accept;
	private JButton m_decline;

	private boolean m_accepted;

	public AcceptNotification() {
		m_accept = new JButton("Accept");
		m_decline = new JButton("Decline");

		m_accept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				superHide();

				m_accepted = true;
				synchronized (AcceptNotification.this) {
					AcceptNotification.this.notifyAll();
				}
			}
		});
		m_decline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				superHide();

				m_accepted = false;
				synchronized (AcceptNotification.this) {
					AcceptNotification.this.notifyAll();
				}
			}
		});

		setButtonDimensions(new Dimension(100, 22));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_decline);
		buttonPanel.add(m_accept);
		this.addComponent(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Will wait for the user to click a button (if the Notification hides, this method will act as if the user clicked
	 * deny).
	 *
	 * @return the user's response
	 */
	public boolean blockUntilReply() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return m_accepted;
	}

	/**
	 * Sets the preferred size of the buttons.
	 *
	 * @param d
	 */
	public void setButtonDimensions(Dimension d) {
		m_accept.setPreferredSize(d);
		m_decline.setPreferredSize(d);
	}

	/**
	 * @return the text on the accept button
	 */
	public String getAcceptText() {
		return m_accept.getText();
	}

	/**
	 * Sets the text on the accept button.
	 *
	 * @param acceptText
	 */
	public void setAcceptText(String acceptText) {
		m_accept.setText(acceptText);
	}

	/**
	 * @return the text on the decline button
	 */
	public String getDeclineText() {
		return m_decline.getText();
	}

	/**
	 * Sets the text on the decline button.
	 *
	 * @param declineText
	 */
	public void setDeclineText(String declineText) {
		m_decline.setText(declineText);
	}

	private void superHide() {
		super.hide();
	}

	@Override
	public void hide() {
		super.hide();

		m_accepted = false;
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public void setWindowTheme(WindowTheme theme) {
		super.setWindowTheme(theme);

		// override any color setting done automatically by the WindowTheme
		// since black is the only color that looks good on buttons
		m_accept.setForeground(Color.black);
		m_decline.setForeground(Color.black);
	}
}

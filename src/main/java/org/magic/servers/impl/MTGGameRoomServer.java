package org.magic.servers.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.magic.api.interfaces.MTGCardsProvider.STATUT;
import org.magic.api.interfaces.abstracts.AbstractMTGServer;
import org.magic.game.model.Player;
import org.magic.game.model.Player.STATE;
import org.magic.game.network.actions.AbstractNetworkAction;
import org.magic.game.network.actions.ChangeDeckAction;
import org.magic.game.network.actions.ChangeStatusAction;
import org.magic.game.network.actions.JoinAction;
import org.magic.game.network.actions.ListPlayersAction;
import org.magic.game.network.actions.ReponseAction;
import org.magic.game.network.actions.RequestPlayAction;
import org.magic.game.network.actions.ShareDeckAction;
import org.magic.game.network.actions.SpeakAction;
import org.magic.services.MTGLogger;

public class MTGGameRoomServer extends AbstractMTGServer{
	private transient Logger logger = MTGLogger.getLogger(this.getClass());
	private IoAcceptor acceptor;
	private IoHandlerAdapter adapter = new IoHandlerAdapter() {
 		
		private void playerUpdate(ChangeStatusAction act) {
			((Player)acceptor.getManagedSessions().get(act.getPlayer().getId()).getAttribute("PLAYER")).setState(act.getPlayer().getState());	
		}
		

		private void sendDeck(ShareDeckAction act) {
			acceptor.getManagedSessions().get(act.getTo().getId()).write(act);
		}

		
		private void join(IoSession session, JoinAction ja)
		{
			if(!getString("MAX_CLIENT").equals("0")&&acceptor.getManagedSessions().size()>=Integer.parseInt(getString("MAX_CLIENT")))
			{
					session.write(new SpeakAction(null,"Number of users reached (" + getString("MAX_CLIENT") +")"));
					session.closeOnFlush();
					return;
			}
			ja.getPlayer().setState(STATE.CONNECTED);
			ja.getPlayer().setId(session.getId());
			session.setAttribute("PLAYER",ja.getPlayer());
			speak(new SpeakAction(ja.getPlayer(), " is now connected"));
			session.write(session.getId());
			
			refreshPlayers(session);
		}
		
		@Override
 		public void sessionCreated(IoSession session) throws Exception {
 			logger.debug("New Session " + session.getRemoteAddress());
 			session.write(new SpeakAction(null, getString("WELCOME_MESSAGE")));
 		}
 	 	
 	 	@Override
 		public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
 	 		refreshPlayers(session); //refresh list users
 		}
 	 
 	 	@Override
 	 	public void messageReceived(IoSession session, Object message) throws Exception {
 	 		logger.info(message);
 	 		if(message instanceof AbstractNetworkAction)
 	 		{
 	 			AbstractNetworkAction act = (AbstractNetworkAction)message;
 	 			switch (act.getAct()) {
 	 				case REQUEST_PLAY: requestGaming((RequestPlayAction)act);break;
 	 				case RESPONSE: response((ReponseAction)act);break;
 	 				case JOIN: join(session, (JoinAction)act);break;
 	 				case CHANGE_DECK: changeDeck(session,(ChangeDeckAction)act);break;
 	 				case SPEAK: speak((SpeakAction)act);break;	
 	 				case CHANGE_STATUS:playerUpdate((ChangeStatusAction)act);break;
 	 				case SHARE:sendDeck((ShareDeckAction)act);break;
 	 				default:break;
				}
 	 		}
 	 	}
 	 	
 	 	private void response(ReponseAction act) {
 			IoSession s = acceptor.getManagedSessions().get(act.getRequest().getRequestPlayer().getId());
 			IoSession s2 = acceptor.getManagedSessions().get(act.getRequest().getAskedPlayer().getId());
 			s.write(act);
 			s2.write(act);
 		}
 		

 	 	@Override
 	    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
 	    {
 	      logger.error("error sesion",cause);
 	      refreshPlayers(session);
 	    }
	};
	
	
 	@Override
    public String description() {
    	return "Enable local player room server";
    }
	
	public void speak(SpeakAction sa)
	{
		for(IoSession s : acceptor.getManagedSessions().values())
			s.write(sa);
	}
	

	
	
	
	protected void changeDeck(IoSession session, ChangeDeckAction cda) {
			Player p = (Player)session.getAttribute("PLAYER");
			p.setDeck(cda.getDeck());
			session.setAttribute("PLAYER", p);
		
	}

	protected void requestGaming(RequestPlayAction p) {
		IoSession s = acceptor.getManagedSessions().get(p.getAskedPlayer().getId());
		s.write(p);
		
	}


	
	public void refreshPlayers(IoSession session)
	{
		List<Player> list = new ArrayList<>();
			for(IoSession s : acceptor.getManagedSessions().values())
			{
				if(session.getId()!=((Player)s.getAttribute("PLAYER")).getId())
					list.add((Player)s.getAttribute("PLAYER"));
			}
			
		session.write(new ListPlayersAction(list));
	}
	
	public MTGGameRoomServer() throws IOException {
		
		super();
    	acceptor = new NioSocketAcceptor();
        acceptor.setHandler(adapter);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        acceptor.getSessionConfig().setReadBufferSize( Integer.parseInt(getString("BUFFER-SIZE")) );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, Integer.parseInt(getString("IDLE-TIME")) );
	}
	
	 public static void main(String[] args) throws Exception {
		 new MTGGameRoomServer().start();
	 }



	@Override
	public void start() throws IOException {
		 acceptor.bind( new InetSocketAddress(Integer.parseInt(getString("SERVER-PORT"))) );
		 logger.info("Server started on port " + getString("SERVER-PORT") +" ...");
	}



	@Override
	public void stop() throws IOException {
		logger.info("Server closed");
		acceptor.unbind();
	}



	@Override
	public boolean isAlive() {
		return acceptor.isActive();
	}



	@Override
	public boolean isAutostart() {
		return getBoolean("AUTOSTART");
	}



	@Override
	public String getName() {
		return "MTG Game Server";
	}

	@Override
	public STATUT getStatut() {
		return STATUT.BETA;
	}

	@Override
	public void initDefault() {
		setProperty("SERVER-PORT", "18567");
		setProperty("IDLE-TIME", "10");
		setProperty("BUFFER-SIZE", "2048");
		setProperty("AUTOSTART", "false");
		setProperty("WELCOME_MESSAGE", "Welcome to my MTG Desktop Gaming Room");
		setProperty("MAX_CLIENT", "0");
		
	}

	@Override
	public String getVersion() {
		return "1.5";
	}

}

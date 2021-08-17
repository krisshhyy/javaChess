import java.util.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TakenPiecesPanel extends JPanel{

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
   
    public TakenPiecesPanel() {
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new  GridLayout(8,8));
        this.southPanel = new JPanel(new  GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final Table.MoveLog movelog) {
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        Table.MoveLog moveLog = movelog;
        final java.util.List<Piece> whiteTakenPieces = new  ArrayList<>();
        final java.util.List<Piece> blackTakenPieces = new  ArrayList<>();

        for(final Move move : moveLog.getMoves()){
                if (move.isAttack()){
                    final Piece  takenPiece=move.getAttackedPiece();
                    if(takenPiece.getPieceAlliance().isWhite()){
                        whiteTakenPieces.add(takenPiece);
                    }
                    else if (takenPiece.getPieceAlliance().isBlack()){
                        blackTakenPieces.add(takenPiece);
                    }
                    else{
                        throw new RuntimeException("should not reach here");
                    }
                }
            }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>(){

            @Override
            public int compare(Piece o1, Piece o2) {
                // TODO Auto-generated method stub
                return Integer.compare(o1.getPieceValue(),o2.getPieceValue());
            }
            
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>(){

            @Override
            public int compare(Piece o1, Piece o2) {
                // TODO Auto-generated method stub
                return Integer.compare(o1.getPieceValue(),o2.getPieceValue());
            }
            
        });

        for(final Piece takenPiece: whiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File(takenPiece.getPieceAlliance().toString().substring(0,1)
                + takenPiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            }catch(final IOException e){
                e.printStackTrace();;
            }
        }

        for(final Piece takenPiece: blackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File(takenPiece.getPieceAlliance().toString().substring(0,1)
                + takenPiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.northPanel.add(imageLabel);
            }catch(final IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }
}

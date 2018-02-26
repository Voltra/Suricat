package ladysnake.views;

import com.mxgraph.canvas.mxBasicCanvas;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.view.mxCellState;
import ladysnake.helpers.utils.Range;
import ladysnake.models.PendingGraph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**An {@link A_View} dedicated to the execution of the transactions
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class ExecutionView extends A_View{
    protected LockStack lockStack;
    protected PendingGraph pendingGraph;
    protected mxGraphComponent graphComponent;

    /**
     * @see A_View#A_View(ViewsManager)
     */
    public ExecutionView(ViewsManager manager) throws IOException, FontFormatException {
        super(manager);
        if(this.getLockStack() == null)
            this.lockStack = new LockStack();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public LockStack getLockStack() {
        return this.lockStack;
    }

    public PendingGraph getPendingGraph() {
        return pendingGraph;
    }

    public mxGraphComponent getGraphComponent(){
        return graphComponent;
    }

    @Override
    protected ViewPanel setUp() {
        if(this.getLockStack() == null) {
            try {
                this.lockStack = new LockStack();
            } catch (IOException|FontFormatException e) {
                e.printStackTrace();
            }
        }

        ViewPanel panel = new ViewPanel();
        panel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, GRID_SPACING, GRID_SPACING));
        panel.addComponent(TRANSACTION_PANEL, this.getTransactionPanel())
        .addComponent(RHS_PANEL, this.getRhsPanel());

        panel.<ViewPanel>getComponentAs(RHS_PANEL)
        .addComponent(LOCK_STACK_PANEL, this.getLockStackPanel())
        .addComponent(PENDING_GRAPH_PANEL, this.getPendingGraphPanel());

        return panel;
    }

    @Override
    public String getViewTitle() {
        return "Suricat - Exécution";
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s right hand side panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getRhsPanel(){
        ViewPanel p = new ViewPanel();
        p.setLayout(new GridLayout(RHS_ROWS, RHS_COLS, GRID_SPACING, GRID_SPACING));
        return p;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s left hand side panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getTransactionPanel(){
        ViewPanel p = new ViewPanel();
        p.setLayout(new GridLayout(1,1));
        ViewPanel innerPanel = new ViewPanel();
        for(char c : Range.make('a', 'z'))
            innerPanel.addComponent(TRANSACTION_PANEL + "_" + c, new JButton(TRANSACTION_PANEL+ "_" + c));

        final String SCROLLPANE = "scroll";
        p.addComponent(SCROLLPANE, new JScrollPane(innerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        return p;
    }

    protected ScrollPaneLayout getNewBothScrollingLayout(){
        ScrollPaneLayout scrollPaneLayout = new ScrollPaneLayout();
        scrollPaneLayout.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneLayout.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPaneLayout;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s lock stack panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getLockStackPanel(){
        ViewPanel p = new ViewPanel();
        p.setLayout(new GridLayout());
//        p.addComponent(LOCK_STACK, lockStack);
        ViewPanel innerPanel = new ViewPanel();
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.addComponent(LOCK_STACK, this.getLockStack());
        JScrollPane scrollPane = new JScrollPane(innerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(this.getLockStack());
        p.addComponent(LOCK_STACK, scrollPane);
        return p;
    }

    /**Retrieves the {@link ViewPanel} the constitutes this {@link A_View}'s pending graph panel
     * @return the constructed {@link ViewPanel}
     */
    protected ViewPanel getPendingGraphPanel(){
        ViewPanel p =new ViewPanel();
        p.setLayout(new GridLayout());
        this.pendingGraph = new PendingGraph();
        VisualPendingGraph graph = new VisualPendingGraph(this.pendingGraph);
        this.graphComponent = new mxGraphComponent(graph);
        mxGraphOutline graphOutline = new mxGraphOutline(this.graphComponent);
        JScrollPane scrollPane = new JScrollPane(graphOutline, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.addComponent(PENDING_GRAPH, scrollPane);
        scrollPane.add(graphComponent);
        return p;
    }

    @Override
    public JMenuBar getViewMenuBar() { return null; }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Class properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    public final static int GRID_ROWS = 1;
    public final static int GRID_COLS = 2;
    public final static int GRID_SPACING = 10;

    public final static int RHS_ROWS = 2;
    public final static int RHS_COLS = 1;

    public final static String TRANSACTION_PANEL = "Transaction";
    public final static String RHS_PANEL = "rhs-panel";
    public final static String LOCK_STACK_PANEL = "Locks";
    public final static String PENDING_GRAPH_PANEL = "Pendings";
    public final static String LOCK_STACK = "lock-stack";
    public final static String LOCK_STACK_INNER = "stack-inner";

    public final static String PENDING_GRAPH = "ExecutionController@pendingGraph";
}

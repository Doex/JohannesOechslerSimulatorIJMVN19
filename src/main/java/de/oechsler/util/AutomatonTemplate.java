package de.oechsler.util;

public class AutomatonTemplate {

    public static final String DEFAULT_TEMPLATE = """
            import de.oechsler.model.Automaton;
            import de.oechsler.model.Cell;

            public class {0} extends Automaton {
                final private static int initNumberOfRows = 50;
                final private static int initNumberOfColumns = 50;
                final private static int numberOfStates = 2;
                final private static boolean mooreNeighborhood = true;
                final private static boolean initTorus = true;
                
                public {0}() {
                    super(initNumberOfRows, initNumberOfColumns,
                    numberOfStates, mooreNeighborhood,
                    initTorus);
                }
                
                protected Cell transform(Cell cell, Cell[] neighbors) {
                    return cell;
                }
            }
            """;

    public static final String GOL_TEMPLATE = """
            import de.oechsler.model.Automaton;
                import de.oechsler.model.Callable;
                import de.oechsler.model.Cell;
                
                
                  public class {0} extends Automaton {
                final private static int initNumberOfRows = 50;
                final private static int initNumberOfColumns = 50;
                final private static int numberOfStates = 2;
                final private static boolean mooreNeighborhood = true;
                final private static boolean initTorus = true;
                
                public {0}() {
                    super(initNumberOfRows, initNumberOfColumns,
                    numberOfStates, mooreNeighborhood,
                    initTorus);
                }
               
                    @Override
                    protected synchronized Cell transform(Cell cell, Cell[] neighbors) {
                        int count = 0;
                        for (Cell tmpCell : neighbors) {
                            if(tmpCell == null){
                                continue;
                            }
                            if (tmpCell.getState() != 0) {
                                count++;
                            }
                        }
                        if ((count == 3) && cell.getState() == 0) {
                            return new Cell(1);
                        }
                        if (count < 2) {
                            return new Cell(0);
                        }
                        if ((count == 2 || count == 3) && (cell.getState() == 1)) {
                            return new Cell(1);
                        }
                        if (count > 3 && cell.getState() == 1) {
                            return new Cell(0);
                        }
                
                        return new Cell(0);
                    }
                             @Callable
                             public void setGlider(int row, int col){
                                 setState(row, col, 1);
                                 setState(row + 1, col + 1, 1);
                                 setState(row + 2, col - 1, 1);
                                 setState(row + 2, col, 1);
                                 setState(row + 2, col + 1, 1);
                                 notifyObserver();
                             }
                         
                             @Callable
                             public void setOscillator(int row, int col){
                                 setState(row, col, 1);
                                 setState(row + 1, col, 1);
                                 setState(row + 2, col, 1);
                                 notifyObserver();
                             }
                            
                }
                
            """;

    public static final String KRUEMELMONSTER_TEMPLATE = """          
                        
            import de.oechsler.model.Automaton;
            import de.oechsler.model.Cell;
            import java.util.Arrays;
            import java.util.Optional;
                        
            public class {0} extends Automaton {
                public {0}(int rows, int columns, boolean isTorus) {
                    super(rows, columns, 10, true, isTorus);
                }
                public {0}() {
                    this(100, 100, true);
                }
                @Override
                protected synchronized Cell transform(Cell cell, Cell[] neighbors) {
                    if (cell.getState() == super.getNumberOfStates() - 1) {
                        return new Cell(0);
                    }
                    Optional<Cell> candidate = Arrays.stream(neighbors)
                            .filter(c -> c != null && c.getState() == cell.getState() + 1)
                            .findFirst();
                    return candidate.map(value -> new Cell(value.getState()))
                            .orElseGet(() -> new Cell(cell.getState()));
                }
            }
              
            """;
}

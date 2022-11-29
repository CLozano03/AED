
package aed.tries;

import java.util.Arrays;
import java.util.Iterator;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.tree.GeneralTree;
import es.upm.aedlib.tree.LinkedGeneralTree;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;

public class DictImpl implements Dictionary {
  // A boolean because we need to know if a word ends in a node or not
  GeneralTree<Pair<Character, Boolean>> tree;

  public DictImpl() {
    tree = new LinkedGeneralTree<>();
    tree.addRoot(new Pair<Character, Boolean>(null, false));
  }

  // Metodos auxiliares

  // Devuelve el nodo cuyo camino desde la raiz contiene
  // la palabra prefix. Si no existe, el metodo devuelve null.
  public Position<Pair<Character, Boolean>> findPos(String prefix) {
    Position<Pair<Character, Boolean>> nodo = tree.root();
    boolean existe = true;

    for (int i = 0; i < prefix.length() && existe; i++) {
      nodo = searchChildLabelledBy(prefix.charAt(i), nodo);
      existe = nodo != null;
    }
    if (!existe) {
      return null;
    }
    return nodo.element().getRight() == true ? nodo : null;
  }

  // Devuelve el hijo del nodo pos que contiene el caracter ch.
  public Position<Pair<Character, Boolean>> searchChildLabelledBy(char ch, Position<Pair<Character, Boolean>> pos) {
    Position<Pair<Character, Boolean>> res = null;
    for (Position<Pair<Character, Boolean>> hijo : tree.children(pos)) {
      if (hijo.element().getLeft().equals(ch)) {
        res = hijo;
      }
    }
    return res;
  }

  // Anade un hijo al nodo pos conteniendo el elemento pair,
  // respetando el orden alfabetico de los hijos.
  public Position<Pair<Character, Boolean>> addChildAlphabetically(Pair<Character, Boolean> pair,
      Position<Pair<Character, Boolean>> pos) {
    Iterator<Position<Pair<Character, Boolean>>> it = tree.children(pos).iterator();
    Position<Pair<Character, Boolean>> hijo = null;
    boolean encontrado = false;
    if (!it.hasNext()) {
      return tree.addChildFirst(pos, pair);
    }

    while (it.hasNext() && !encontrado) {
      hijo = it.next();
      encontrado = hijo.element().getLeft().compareTo(pair.getLeft()) > 0;
    }
    return encontrado? tree.insertSiblingBefore(hijo, pair): tree.insertSiblingAfter(hijo, pair);
  }

  /* ------------------------------------------------------------------------------ */
  /* ------------------------------------------------------------------------------ */
  @Override
  public void add(String word) {
    if (word == null || word.isEmpty())
      throw new IllegalArgumentException();
      addAux(word, 0, tree.root());
  }

  public void addAux(String word, int i, Position<Pair<Character, Boolean>> parent) {
    Position<Pair<Character, Boolean>> hijo = searchChildLabelledBy(word.charAt(i), parent);
    if (hijo == null) {
      if (i == word.length() - 1) {
        addChildAlphabetically(new Pair<Character, Boolean>(word.charAt(i), true), parent);
      } else {
        hijo = addChildAlphabetically(new Pair<Character, Boolean>(word.charAt(i), false), parent);
        addAux(word, i + 1, hijo);
      }
    } else {
      if (i == word.length() - 1 && !hijo.element().getRight()) {
        hijo.element().setRight(true);
      } else if (!(i == word.length() - 1 && hijo.element().getRight())){
        addAux(word, i + 1, hijo);
      }
    }
  }

  @Override
  public void delete(String word) {
    if (word == null || word.isEmpty())
      throw new IllegalArgumentException();

    Position<Pair<Character, Boolean>> posicionUlt = findPos(word);
    if (posicionUlt != null) {
      posicionUlt.element().setRight(false);
    }
  }

  @Override
  public boolean isIncluded(String word) {
    if (word == null || word.isEmpty()) {
      throw new IllegalArgumentException();
    }
    return findPos(word) != null;
  }
  
  @Override
  public PositionList<String> wordsBeginningWithPrefix(String prefix) {
    Position<Pair<Character, Boolean>>  P = tree.root();
    boolean existe = true;

    for (int i = 0; i < prefix.length() && existe; i++) {
      P = searchChildLabelledBy(prefix.charAt(i), P);
      existe = P != null;
    }
    if (!existe) {
      return null;
    }

    PositionList<String> result = new NodePositionList<String>();
      
    wordsBeginningWithPrefixAux(P, result, prefix);
    return result;

  }
  
  public void wordsBeginningWithPrefixAux(Position<Pair<Character, Boolean>> P, PositionList<String> result, String actual){
    if(P.element().getRight()){
      result.addLast(actual);
    } 
    for(Position<Pair<Character, Boolean>> child: tree.children(P)){
      wordsBeginningWithPrefixAux(child, result, actual + child.element().getLeft());
    }
  }
}

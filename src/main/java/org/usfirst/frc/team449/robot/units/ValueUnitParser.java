package org.usfirst.frc.team449.robot.units;

import com.google.common.collect.PeekingIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public class ValueUnitParser {
  private ValueUnitParser() {
  }

//    /**
//     *
//     * @param tokenizer
//     * @return {@code tokenizer}
//     */
//    private static StreamTokenizer configureTokenizer(final StreamTokenizer tokenizer) {
//        tokenizer.resetSyntax();
//        tokenizer.wordChars(0,255);
//        tokenizer.whitespaceChars(0, ' ');
//        tokenizer.parseNumbers();
//        return tokenizer;
//    }

  public static void main(final String[] args) throws IOException, ClassNotFoundException {
    new TokenIterator("(2m * 3 ft) /7 rad + 92").forEachRemaining(System.out::println);
    System.out.println(parseExpr("(2m * 3 ft) /7 rad + 92"));
  }

  public static NormalizedUnitValue<?> parseExpr(final String source) {
    final TokenIterator tokens = new TokenIterator(source);
    return parseExpr(tokens);
  }

  private static NormalizedUnitValue<?> parseExpr(final TokenIterator tokens) {
    return parseExpr(tokens, parsePrimary(tokens), Integer.MIN_VALUE);
  }

  private static NormalizedUnitValue<?> parseExpr(final TokenIterator source, NormalizedUnitValue<?> lhs, final int minPrecedence) {
    Token lookahead = source.peek();
    while (lookahead.type == Token.Type.OPERATOR && Token.OPERATORS.get(lookahead.value) >= minPrecedence) {
      final Token op = lookahead;
      final int opPrecedence = Token.OPERATORS.get(op.value);

      source.next();

      NormalizedUnitValue<?> rhs = parsePrimary(source);

      while (source.hasNext() && (lookahead = source.peek()).type == Token.Type.OPERATOR && Token.OPERATORS.get(lookahead.value) > opPrecedence) {
        rhs = parseExpr(source, rhs, Token.OPERATORS.get(lookahead.value));
        lookahead = source.peek();
      }

      switch (op.value) {
        case "*":
          lhs = new Product(lhs, rhs);
          break;
        case "/":
          lhs = new Quotient(lhs, rhs);
          break;
        case "+":
          lhs = new Sum(lhs, rhs);
          break;
        case "-":
          lhs = new Difference(lhs, rhs);
          break;
      }

      if (!source.hasNext()) break;
    }

    return lhs;
  }

  private static NormalizedUnitValue<?> parsePrimary(final TokenIterator source) {
    if (source.peek().type == Token.Type.OPEN_PAREN) {
      source.next();

      final var result = parseExpr(source);

      if (source.next().type != Token.Type.CLOSE_PAREN)
        throw new IllegalStateException("Unmatched opening parenthesis.");

      return result;

    } else {
      final Token first = source.next();

      if (first.type != Token.Type.NUMBER)
        throw new IllegalStateException("Expected a number.");

      final double value = Double.parseDouble(first.value);

      final String unit;
      if (source.hasNext() && source.peek().type == Token.Type.WORD) {
        unit = source.next().value;
      } else {
        unit = Dimensionless.UNIT.getShortUnitName();
      }

      return DimensionUnitValue.fromUnitName(unit).withValue(value);
    }
  }
}

class Token {
  public Token(final Type type, final String value) {
    this.type = type;
    this.value = value;
  }

  public final Type type;
  public final String value;

  @Override
  public String toString() {
    return this.type + " " + this.value;
  }

  public static Type classify(final char c) {
    if (c == '(') return Type.OPEN_PAREN;
    if (c == ')') return Type.CLOSE_PAREN;
    if (OPERATORS.containsKey(String.valueOf(c))) return Type.OPERATOR;
    if (Character.isWhitespace(c)) return Type.WHITESPACE;
    if (c == '.' || Character.isDigit(c)) return Type.NUMBER;
    return Type.WORD;
  }

  public enum Type {
    WHITESPACE, OPEN_PAREN, CLOSE_PAREN, WORD, NUMBER, OPERATOR
  }

  public static final Map<String, Integer> OPERATORS = Map.of("*", 0, "/", 0, "+", -1, "-", -1);
}

class TokenIterator implements PeekingIterator<Token> {
  private final String source;

  private int pos;
  private char curChar;
  @NotNull
  private Token.Type curType;
  @Nullable
  private Token next;

  public TokenIterator(final String source) {
    this.source = source;
    this.pos = 0;
    this.curChar = source.charAt(0);
    this.curType = Token.classify(this.curChar);
    this.next = this.getNext();
  }

  @Override
  public boolean hasNext() {
    return this.next != null;
  }

  @Override
  @NotNull
  public Token peek() throws NoSuchElementException {
    if (this.next == null) throw new NoSuchElementException();
    return this.next;
  }

  @Override
  @NotNull
  public Token next() throws NoSuchElementException {
    final var result = this.peek();
    this.next = this.getNext();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove() throws IllegalStateException {
    throw new IllegalStateException("Unsupported.");
  }

  @Nullable
  private Token getNext() {
    while (true) {
      final Token t = this.getWithWhitespace();
      if (t == null || t.type != Token.Type.WHITESPACE) return t;
    }
  }

  @Nullable
  private Token getWithWhitespace() {
    if (this.pos >= this.source.length()) return null;

    final int curRunStart = this.pos;

    while (this.pos < this.source.length()) {
      final char prevChar = this.curChar;
      final Token.Type prevType = this.curType;

      this.curChar = this.source.charAt(this.pos);
      this.curType = Token.classify(this.curChar);

      if (this.curType != prevType) {
        return new Token(prevType, this.source.substring(curRunStart, this.pos));
      }

      this.pos++;
    }

    return new Token(this.curType, this.source.substring(curRunStart, this.pos));
  }
}

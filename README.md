# scala-wordcloud

A very simple yet advanced wordcloud with supplementary NLP components.

### Tokenization

The `tokenize` package supplies classes for sentence splitting and
tokenization. The used algorithms apply some heuristics to achieve
relative good results.

example:

    import scala.io.Source
    
    val tokenizer = new wordcloud.pos.TokenizerDE()
    val tokenizedSents = tokenizer.tokenizedSents(Source.fromFile("file"))

### Part of Speech Tagging

The package `pos` supplies classes for POS tagging with a
perceptron-like algorithm.

example:

    val tagger = new wordcloud.pos.PosTaggerDE()
    tagger.train(cReader)
    // cReader is an instance of wordcloud.utils.corpus.CorpusReader
    
    tagger.tag("Ich finde Scala gut .".split(" "))
    // possible result: Array[String] = Array(PPER, VVFIN, NN, ADV, $.)

Note that `tag()` tags *one* tokenized sentence (array of tokens).

Saving and loading a tagger is easy:

    import java.io._
    
    val outp = new ObjectOutputStream(new FileOutputStream("saved_tagger"))
    outp.writeObject(tagger)
    outp.close()
    
    val inp = new ObjectInputStream(new FileInputStream("saved_tagger"))
    val tagger = inp.readObject.asInstanceOf[wordcloud.pos.PosTagger]
    inp.close()
    
    

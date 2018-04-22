package structures;

import java.util.ArrayList;

/**
 * This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 * indexes, first child and sibling.
 * 
 *
 */
public class Trie {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}
	
	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 * 
	 * @param word Word to be inserted.
	 */
	public void insertWord(String word)
	{
		word = word.toLowerCase(); 
		
		if (word == null || word.length() == 0) //to make sure there is correct input
		{ 
			return; 
		}
		for (int i = 0; i < word.length(); i++)
		{ 
			if (word.charAt(i) >= 'a' && word.charAt(i) <= 'z')  //SHOULD BE LETTERS STRICTLY
			{ 
				continue; 
			}
			else 
			{ 
				return;
			}
		}
		
		words.add(word); 
		int cnt = 0; 
		
		while (cnt < this.words.size())
		{ // counter doesnt exceed array size
			this.insertNode(this.root, (short) 0, words, cnt, "first"); 
			cnt++; 
		}
	}
	
	private void insertNode (TrieNode root, short ci, ArrayList<String> words, int wi, String ch)
	{
		TrieNode ptr = root; 
		
		if (ch.equals("sibling"))  ///making sure if its a sibling
		{ 
			if (root != null)
			{ 
				root = root.sibling; 
			}
			
		}
		if (ch.equals("first"))   //making sure its a parent
		{ 
			if (root != null) 
			{ 
				root = root.firstChild; 
			}
		}
		if (root == null) 
		{ 
			
			Indexes nind = new Indexes(0, (short) 0, (short) 0); 
			
			nind.wordIndex = wi; 
			nind.startIndex = ci;
			nind.endIndex = (short) (words.get(wi).length()-1); 
			
			TrieNode tfc = null;
			TrieNode sibtemp = null; 
			TrieNode sibnew = new TrieNode (nind, tfc, sibtemp); 
			
			if (ch.equals("sibling"))
			{ 
				ptr.sibling = sibnew;
			}	
			
			if (ch.equals("first"))
			{ 
				ptr.firstChild = sibnew; 
			}
		}
		else
		{ 
			String nch = null; 
			
			String commpare = words.get(root.substr.wordIndex).substring(root.substr.startIndex, root.substr.endIndex + 1);
			
			if (words.get(wi).length() >= root.substr.endIndex + 1) 
			{ 
				nch = words.get(wi).substring(root.substr.startIndex, root.substr.endIndex + 1); 
			}
			
			else
			{ 
				nch = words.get(wi).substring(root.substr.startIndex, words.get(wi).length()); 
			}
			
			if (commpare.equals(nch) && (words.get(wi).length() > root.substr.endIndex + 1))
			{ 
				insertNode (root, 
						(short) (root.substr.endIndex + 1), 
						words, 
						wi, 
						"first"); //WE US RECURSION
				
				return; 
			}
			
			if (commpare.equals(nch) 
					&& (words.get(wi).length() <= root.substr.endIndex + 1))
			{ 
				return; 
			}
			
			int counter = ci; 
			
			
			
			while (words.get(wi).charAt(counter) == words.get(root.substr.wordIndex).charAt(counter) 
					&& (counter < root.substr.endIndex && counter < words.get(wi).length() - 1)) 
			{ 
				counter++; 
			}
			
			
			
			
			if (words.get(wi).charAt(counter)
					== words.get(root.substr.wordIndex).charAt(counter)) 
			{ 
				if (root.firstChild != null)
				{ 
					counter++; 
				}
				else
				{ 
					return; 
				}
			}
			
			if (counter == ci) 
			{ 
				insertNode (root, (short) counter, words, wi, "sibling"); //AGAIIIIIN
			}
			
			
			else if ((counter != ci) && counter <= root.substr.endIndex)
			{ 
				Indexes fin = new Indexes (0, (short) 0, (short) 0); 
				
				
				fin.wordIndex = root.substr.wordIndex;
				fin.startIndex = (short) counter; 
				fin.endIndex = root.substr.endIndex; 
				
				TrieNode fin1 = new TrieNode (fin, null, null); 
				
				
				fin1.substr.wordIndex = root.substr.wordIndex; 
				fin1.substr.startIndex = (short) counter; 
				fin1.substr.endIndex = root.substr.endIndex;
				
				root.substr.endIndex = (short) (counter - 1);
				
				
				fin1.firstChild = root.firstChild; 
				
				root.firstChild = fin1; 
				
				root = root.firstChild; 
				//recursion again
				insertNode (root, (short) counter, words, wi, "sibling"); 
				
				return; 
				
			}
			
			
			
			
			else 
			{ 
				if (root.firstChild != null) 
				{ 
					insertNode (root, (short) counter, words, wi, "first"); 
				}
				else 
				{ 
					return; 
				}
			}
		}
 	}
	
	
	/**
	 * Given a string prefix, returns its "completion list", i.e. all the words in the trie
	 * that start with this prefix. For instance, if the tree had the words bear, bull, stock, and bell,
	 * the completion list for prefix "b" would be bear, bull, and bell; for prefix "be" would be
	 * bear and bell; and for prefix "bell" would be bell. (The last example shows that a prefix can be
	 * an entire word.) The order of returned words DOES NOT MATTER. So, if the list contains bear and
	 * bell, the returned list can be either [bear,bell] or [bell,bear]
	 * 
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all words in tree that start with the prefix, order of words in list does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 *         
	 */
	
	
	private TrieNode help (ArrayList<String> list, TrieNode ncurr) 
	{ 
		if (ncurr == null)
		{
			return ncurr;
		}
		
		
		
		if (ncurr.firstChild == null) 
		{
			list.add(words.get(ncurr.substr.wordIndex));
			
			help(list, ncurr.sibling);
			
			return null;
		} 
		
		
		else 
		{ 
			if (ncurr.firstChild != null) 
			{
				help (list, ncurr.firstChild);
			}
			if (ncurr.sibling != null) 
			{
				help (list, ncurr.sibling);
			}
			return null;
		}
	}
	
	
	
	
	public ArrayList<String> completionList(String prefix) 
	{
		ArrayList<String> list = new ArrayList<String>();
		
		TrieNode ncurr = this.root.firstChild; 
		
		
		while (ncurr != null)
		{ 
			int si = ncurr.substr.startIndex;
			
			int ei = ncurr.substr.endIndex;
			if (si > prefix.length()-1)
			{
				ncurr = help(list, ncurr);
			}
			
			else if (prefix.charAt(si) == words.get(ncurr.substr.wordIndex).charAt(si)) 
			{
				
				if (prefix.length() >= ei + 1)
				{
					if (prefix.substring(si, ei + 1).equals(words.get(ncurr.substr.wordIndex).substring(si,ei + 1))) 
					{
						if (ncurr.firstChild == null)
						{
							list.add(words.get(ncurr.substr.wordIndex));
						}
						ncurr = ncurr.firstChild;
					} 
					else
					{
						return list;
					}
				} 
				
				else
				{
					if (prefix.substring(si).equals(words.get(ncurr.substr.wordIndex)
							.substring(si,prefix.length())))
					{
						if(ncurr.firstChild == null) 
						{
							list.add(words.get(ncurr.substr.wordIndex));
							
							return list;
						}
						else
						{
							ncurr = help(list, ncurr.firstChild);
						}
					}
					else
					{
						return list;
					}
				}
			} 
			
			else
			{
				ncurr = ncurr.sibling;
			}
			
		}
		return list;
	}
	
	
	public void print() {
		print(root, 1, words);
	}
	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }

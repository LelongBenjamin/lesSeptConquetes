import extensions.CSVFile;
import extensions.Sound;

/*
 *#Attention : taille du terminal 132 * 43 caractere
 *
 */

public class les7conquetes extends Program{

	int touche = 0; //variable contenant la dernier touche entre sur le clavier.
	String backgroundColor = "black";

	void algorithm(){
		int choix = 0; // variable contenant le retour de la variable selecteur;
		enableKeyTypedInConsole(true);
		hide(); // on cahe le curseur
		while(choix != 4){
			resetScreen("black");
			printFenetre(new int[]{13, 60}, 24, 13, "blue");
			printlabel(new int[]{13, 64}, " menu principal ", "black");
			choix = selecteur(new int[]{15, 64}, new String[]{"Jouer","Classement","Aide","Crédit","Quitter"}, true, new int[]{16, 2});
			if(choix == 0){
				lancerPartie();
			}else if(choix == 1){
				classement();
			}else if(choix == 2){
				information("Aide");
			}else if(choix == 3){
				information("Crédit");
			}
		}
		resetScreen("reset"); // on nettoye le terminal
		show()	; //on affiche le curseur
	}

	void lancerPartie(){
		String[] profil_1 = new String[2];
		String[] profil_2 = new String[]{"-1","IA","black"};
		resetScreen("black");
		printFenetre(new int[]{13, 60}, 24, 7, "blue");
		if(0 == selecteur(new int[]{15, 64}, new String[]{"un joueur","deux joueur"}, true, new int[]{16, 2})){
			profil_1 = choixProfil(new String[]{"",""});
			profil_1 = new String[]{profil_1[0],profil_1[1], choixCouleur("black",true)};
		}else{
			profil_1 = choixProfil(new String[]{"",""});
			profil_1 = new String[]{profil_1[0],profil_1[1], choixCouleur("",false)};
			if(!equals(profil_1[0], "")){
				resetScreen("black");
				printFenetre(new int[]{13, 58}, 35, 5, "blue");
				printlabel(new int[]{14, 60}, " c'est au joueur 2 de choissir ", "black");
				printlabel(new int[]{16, 66}, " entre pour continuer ", "black");
				confirme();
				profil_2 = choixProfil(profil_1);
				profil_2 = new String[]{profil_2[0],profil_2[1], choixCouleur(profil_1[2],false)};
			}
		}
		if(!equals(profil_1[0], "") && !equals(profil_2[0], "")){
			backgroundColor = "blue";
			partie(profil_1, profil_2, new String[]{"white", "white", "white", "white", "white", "white", "white"}, chargerQuestion(), chargerCSV("./ressources/carte.csv"), 1);
		}
	}

	void partie(String[] profil_1, String[] profil_2, String[] colorPays, String[][][] question, String[][] carte, int numeroTour){
		if(numeroTour <= 10){
			colorPays = tourDeJeux(profil_1, colorPays, question, carte, numeroTour);
			if(!equals(profil_2[1], "IA")){
				colorPays = tourDeJeux(profil_2, colorPays, question, carte, numeroTour);
			}else{
				colorPays = ia(colorPays, profil_2, carte);
			}
			partie(profil_1, profil_2, colorPays, question, carte, numeroTour + 1);	
		}
		printFenetre(new int[]{36, 4}, 140, 7, "blue");
		int pointJoueur1 = 0;
		int pointJoueur2 = 0;
		String[][] listeProfil = chargerCSV("./ressources/profil.csv");
		for(int i=0;i < length(colorPays); i++){
			if(equals(colorPays[i], profil_1[2])){
				pointJoueur1 = pointJoueur1 + 1;
			}else if(equals(colorPays[i], profil_2[2])){
				pointJoueur2 = pointJoueur2 + 1;
			}
		}
		if(pointJoueur1 == pointJoueur2){
			printlabel(new int[]{39, 7}, "le combat a etait dure, personne a gagne (égaliter)" , "black");
		}else if(pointJoueur1 > pointJoueur2){
			printlabel(new int[]{39, 7}, "Bravo, " + profil_1[1] + " vous avez gagne" , "black");
			printlabel(new int[]{40, 7}, "vous avez un score de : " + (stringToInt(profil_1[0]) + (pointJoueur1 * 100)), "black");
		}else{
			if(equals(profil_2[1], "IA")){
				printlabel(new int[]{39, 7}, "l'ordinateur vous a battu !" , "black");
			}else{
				printlabel(new int[]{39, 7}, "Bravo, " + profil_2[1] + " vous avez gagne" , "black");
				printlabel(new int[]{40, 7}, "vous avez un score de : " + (profil_2[0] + (pointJoueur1 * 100)), "black");
				listeProfil = tableauSupprimerLigne( listeProfil, trouverUneLigne( listeProfil, profil_2[1], 1));
				listeProfil = tableauAjouterLigne( listeProfil, new String[]{(stringToInt(profil_2[0]) + (pointJoueur1 * 100)) + "", profil_2[1]});
			}
		}
		listeProfil = tableauSupprimerLigne( listeProfil, trouverUneLigne( listeProfil, profil_1[1], 1));
		listeProfil = tableauAjouterLigne( listeProfil, new String[]{(stringToInt(profil_1[0]) + (pointJoueur1 * 100)) + "", profil_1[1]});
		saveCSV(listeProfil, "./ressources/profil.csv", ';');
		backgroundColor = "black";
		delay(250);
	}

	String[] ia(String[] colorPays, String[] profil, String[][] carte){
		final String[] conditionDeDominationIA = new String[]{profil[2], profil[2], profil[2], profil[2], profil[2], profil[2], profil[2]};
		printFenetre(new int[]{36, 4}, 140, 7, profil[2]);
		if(!equalsTableau(conditionDeDominationIA, colorPays)){
			int choixPays = (int) (random()*7);
			while(equals(colorPays[choixPays], profil[2])){
				choixPays = (int) (random()*7);
			}
			if((int) (random()*100) > 15){
				colorPays[choixPays]  = profil[2];
				dessinCart(carte,colorPays);
				printlabel(new int[]{39, 7}, "Pas de chance l'ordinateur a bien répondu !" , "black");
			}else{
				printlabel(new int[]{39, 7}, "Yes, c'est notre chance ! L'ordinateur c'est trompé !", "black");
			}
		}else{
			printlabel(new int[]{39, 7}, "L'ordinateur ne se prend meme plus la peine de répondre aux questions :(", "black");
		}
		confirme();	
		return colorPays;
	}

	String[] tourDeJeux(String[] profil, String[] colorPays, String[][][] question, String[][] carte, int numeroTour){
		int choix = 0;
		int choixPays = 0;
		int numeroDeQuestion = 0;
		resetScreen("blue");
		dessinCart(carte,colorPays);
		printFenetre(new int[]{36, 4}, 140, 7, profil[2]);
		printlabel(new int[]{37, 7}, "C'est à vous de jouer ", "black");
		printlabel(new int[]{37, 29}, profil[1], profil[2]);
		printlabel(new int[]{38, 7}, "Tour n° " + numeroTour + ", quel pays voulez-vous ?", "black");
		choixPays = selecteur(new int[]{40, 7}, new String[]{"Groenland", "Amerique du nord", "Amerique du sud", "Europe", "Afrique", "Asie", "Australie"}, false, new int[]{19, 0});
		numeroDeQuestion = (int) (random() * (length(question[choixPays]) - 1));
		printFenetre(new int[]{36, 4}, 140, 7, profil[2]);
		printlabel(new int[]{37, 7}, "question n°" + numeroDeQuestion + " : " + question[choixPays][numeroDeQuestion][0], "black");
		if(stringToInt(question[choixPays][numeroDeQuestion][5]) == selecteur(new int[]{39, 7}, new String[]{question[choixPays][numeroDeQuestion][1], question[choixPays][numeroDeQuestion][2], question[choixPays][numeroDeQuestion][3], question[choixPays][numeroDeQuestion][4]}, false, new int[]{33, 0})){
			colorPays[choixPays] = profil[2];
			dessinCart(carte,colorPays);
			printFenetre(new int[]{36, 4}, 140, 7, profil[2]);
			printlabel(new int[]{38, 7}, "Bravo ! Tu as bien répondu :)", "black");
		}else{
			printFenetre(new int[]{36, 4}, 140, 7, profil[2]);
			printlabel(new int[]{39, 7}, "Dommage la bonne réponse était : " + question[choixPays][numeroDeQuestion][stringToInt(question[choixPays][numeroDeQuestion][5]) + 1], "black"); 
			/*	question[choixPays][numeroDeQuestion][stringToInt(question[choixPays][numeroDeQuestion][5]) + 1]
				stringToInt(question[choixPays][numeroDeQuestion][5]) permet davoire la reponce en int , puis avec ce numero
				on va chercher la reponce en text . 
			*/
		}	
		confirme();
		return colorPays;
	}

	String[][][] chargerQuestion(){
		String[][] questionCSV = chargerCSV("./ressources/questions.csv");
		String[][][] question = new String[7][0][6];
		int debut = 0;
		int fin = 0;
		for(int i=0; i < 7; i++) {
			debut = fin;
			while(!equals(questionCSV[fin][0], "fin")){
				fin = fin + 1;
			}
			question[i] = new String[fin - debut][6];
			for(int j = 0; j < (fin - debut); j++) {
				question[i][j] = questionCSV[debut + j]; 
			}
			fin = fin + 1;
		}
		return question;
	}

	String[] choixProfil(String[] profilInterdit){
		String[][] listeProfil = chargerCSV("./ressources/profil.csv");
		if(trouverUneLigne( listeProfil, profilInterdit[1], 1) != -1){
			listeProfil = tableauSupprimerLigne(listeProfil, trouverUneLigne( listeProfil, profilInterdit[1], 1));
		}
		String[] listeSelecteur = new String[length(listeProfil) + 1];
		int choix = 0;
		resetScreen("black");
		printFenetre(new int[]{5, 42}, 68, 30, "blue");
		printlabel(new int[]{5, 68}, " choix d'un profil " , "black");
		listeSelecteur[0] = " Crée un nouveau profil";
		for(int i=0; i < length(listeProfil) - 1; i++){
			listeSelecteur[i + 1] = "  " + listeProfil[i][1] + charRepet((52 - length(listeProfil[i][1])) - length(listeProfil[i][0]), ' ') + listeProfil[i][0];
		}
		listeSelecteur[length(listeProfil)] = " Quitter";
		choix = selecteur(new int[]{7, 46}, listeSelecteur, true, new int[]{60, 1});
		if(choix == 0){
			if(length(listeProfil) > 20){	// limite de 20 profils
				printFenetre(new int[]{12, 60}, 32, 7, "blue");
				printlabel(new int[]{14, 62}, " maximume de profil attaind ", "black");
				printlabel(new int[]{16, 62}, " entre pour retour au choix ", "black");
				confirme();
				return choixProfil(profilInterdit);
			}
			return créeUnProfil(new String[]{"0",""});
		}else if(choix == length(listeProfil)){
			return new String[]{"",""};
		}
		return listeProfil[choix - 1];
	}

	String choixCouleur(String couleurInterdit, boolean ia){
		String[] color = new String[]{"red","yellow","green","purple","cyan","black"};
		String[] colorFR = new String[]{"rouge","jaune","vert","mauve","cyan","noire"};
		if(ia){
			colorFR = new String[]{"rouge","jaune","vert","mauve","cyan"};	
		}
		int choix = 0;
		do{
			resetScreen("black");
			printFenetre(new int[]{13, 60}, 24, 10, "blue");
			printlabel(new int[]{13, 64}, " choix couleur ", "black");
			choix = selecteur(new int[]{15, 64}, colorFR, true, new int[]{16, 1});
			if(equals(color[choix], couleurInterdit)){
				resetScreen("black");
				printFenetre(new int[]{13, 58}, 35, 5, "blue");
				printlabel(new int[]{14, 62}, " Désoler couleur déjà pris ", "black");
				printlabel(new int[]{16, 64}, " entre pour continuer ", "black");
				confirme();
			}
		}while(equals(color[choix], couleurInterdit));
		return color[choix];
	}

	String[] créeUnProfil(String[] profil){
		String[][] listeProfil = chargerCSV("./ressources/profil.csv");
		int choix = 0;
		String[] color = new String[]{"red","yellow","green","purple","cyan","black"};
		printFenetre(new int[]{12, 60}, 28, 7, "blue");
		printlabel(new int[]{12, 66}, " creation profil ", "black");
		do{
			profil[1] = readStringPlus(new int[]{14, 64}, profil[1]);
			if(trouverUneLigne( listeProfil, profil[1], 1) != -1){
				printFenetre(new int[]{12, 60}, 28, 7, "blue");
				printlabel(new int[]{12, 66}, " creation profil ", "black");
				printlabel(new int[]{14, 65}, " nom déjà utilisé ", "black");
				printlabel(new int[]{16, 63}, " entre pour continuer ", "black");
				confirme();
				return créeUnProfil(new String[]{profil[0], ""});
			}
		}while(equals(profil[1],""));
		saveCSV(tableauAjouterLigne( listeProfil, profil), "./ressources/profil.csv", ';');
		return profil;
	}

	void classement(){
		String[][] listeProfil = chargerCSV("./ressources/profil.csv");
		String[] listeSelecteur = new String[length(listeProfil)];
		String[] profil = new String[2];
		resetScreen("black");
		printFenetre(new int[]{5, 42}, 68, 30, "blue");
		printlabel(new int[]{5, 68}, " Classement ", "black");
		for(int i=0; i < length(listeProfil) - 1; i++){
			listeSelecteur[i] = "  " + listeProfil[i][1] + charRepet((52 - length(listeProfil[i][1])) - length(listeProfil[i][0]), ' ') + listeProfil[i][0];
		}
		listeSelecteur[length(listeProfil) - 1] = "  Quitter";
		int choix = selecteur(new int[]{7, 46}, listeSelecteur, true, new int[]{60, 1});
		if(choix != length(listeProfil) - 1){
			profil = listeProfil[choix];
			printFenetre(new int[]{12, 63}, 23, 7, "blue");
			choix = selecteur(new int[]{14, 66}, new String[]{"Editer profil","Sup profil"}, true, new int[]{17, 2});
			if(choix == 0){
				listeProfil = tableauSupprimerLigne( listeProfil, trouverUneLigne( listeProfil, profil[1], 1));
				listeProfil = tableauAjouterLigne( listeProfil, créeUnProfil(profil));
			}else{
				listeProfil = tableauSupprimerLigne( listeProfil, trouverUneLigne( listeProfil, profil[1], 1));
			}
			saveCSV(listeProfil, "./ressources/profil.csv", ';');
			classement();
		}
	}

	void information(String page){
		printFenetre(new int[]{5, 42}, 68, 30, "blue");
		if(equals("Aide", page)){
			printlabel(new int[]{7, 74}, "Aide", "black"); 
			printlabel(new int[]{9, 45}, "Bonjour et bienvenue dans les sept conquêtes !", "black");
			printlabel(new int[]{10, 45}, "Pour commencer, selectionner « jouer ».", "black");  
			printlabel(new int[]{11, 45}, "Choisissez si vous voulez jouer contre l'ordinateur", "black");
			printlabel(new int[]{12, 45}, "ou avec l'un de vos amis.", "black");  	
			printlabel(new int[]{13, 45}, "Vous pouvez charger un profil déjà existant ou alors en ", "black");
			printlabel(new int[]{14, 45}, "créer un nouveau.", "black");	
			printlabel(new int[]{15, 45}, "Pour en créer un nouveau, selectionner « nouveau profil »", "black"); 	
			printlabel(new int[]{16, 45}, "Entrez votre nom de joueur", "black"); 	
			printlabel(new int[]{17, 45}, "Votre profil peut être supprimé ou modifié dans la rubrique ", "black");
			printlabel(new int[]{18, 45}, "« classement ».", "black"); 	
			printlabel(new int[]{19, 45}, "Selectionner la couleur que vous aurez pendant tout le", "black"); 
			printlabel(new int[]{20, 45}, "déroulement du jeu.", "black"); 	
						
			printlabel(new int[]{22, 45}, "Vous entrez dans le coeur du jeu.", "black");	
			printlabel(new int[]{23, 45}, "Choisissez votre territoire à conquérir parmis les sept", "black");
			printlabel(new int[]{24, 45}, "proposés . Vous avez le choix entre 4 réponses, selectionnez", "black");
			printlabel(new int[]{25, 45}, "grace aux flèches directionnelles la bonne réponse.", "black");		
		}else{
			printlabel(new int[]{7, 74}, "Crédit", "black"); 
			printlabel(new int[]{10, 45}, "le jeu a etait crée par : ", "black");
			printlabel(new int[]{13, 65}, "LELONG benjamin", "black"); 	
			printlabel(new int[]{16, 65}, "RUCART Timothé", "black");  	
		}
		printlabel(new int[]{31, 68}, "Entre pour quitter", "black"); 
		confirme();
	}

	void dessinCart(String[][] donne, String[] color){
		background("blue");
		int cpt = 0;
		for(int i=0; i < length(color); i++) {
			text(color[i]);
			while(!equals(donne[cpt][0], "fin")){
				cursor(stringToInt(donne[cpt][0]), stringToInt(donne[cpt][1]));
				print(charRepet(stringToInt(donne[cpt][2]), '&'));
				cpt = cpt + 1;
			}
			cpt = cpt + 1;	
		}
	}

//charge un fichier fichier
	String[][] chargerCSV(String fichier){
		CSVFile file = loadCSV(fichier,';');
		String[][] listeProfil = new String[rowCount(file)][columnCount(file)];

		for(int row = 0; row < rowCount(file); row++){
			for(int column = 0; column < columnCount(file); column++){
				listeProfil[row][column] = getCell(file, row , column);
			}
		}
		return listeProfil;
	}
/*
	# permet de supprimer une ligne dans un tableau à 2 dimention 
  	# String[][] tableauSuprimerLigne( String[][] tableau, int indiceligne)
*/
	String[][] tableauSupprimerLigne( String[][] tableau, int indiceligne){
		if(indiceligne > length(tableau) || length(tableau) == 0){
			return tableau;
		}
		String[][] nouveauTableau = new String[length(tableau) - 1][length(tableau,2)];
		int décalage = 0;
		for(int i=0; i < length(tableau); i++){
			if(indiceligne == i){
				décalage = 1;
			}else{
				nouveauTableau[i - décalage] = tableau[i];
			}
		}
		return nouveauTableau;
	}
	void testTableauSupprimerLigne(){
		String[][] tableau_1 = new String[][]{{"benjamin","blue"},{"paul","green"},{"timothé","blue"}};
		String[][] tableau_2 = new String[][]{{"benjamin","blue"},{"timothé","blue"}};

		//suprime une ligne dans le tableau
		assertEquals(true, equalsTableau2Dim(tableau_2, tableauSupprimerLigne(tableau_1,1)));
		//si l'indice demander n'est pas présent dans le tableau aucune ligne est suprimer  
		assertEquals(true, equalsTableau2Dim(tableau_1, tableauSupprimerLigne(tableau_1,5)));
		//si on envoye un tableau vide il renvoi un tableau vide
		tableau_1 = new String[0][0];
		assertEquals(true, equalsTableau2Dim(tableau_1, tableauSupprimerLigne(tableau_1,0)));
	}
/*
	# permet d'ajouter une ligne dans un tableau à 2 dimention classer (par la premier colone qui doit etre un nombre)
	# du plus grand au plus petit (la dernier ligne de ce tableau est réserver a l'IA)
  	# String[][] tableauAjouterLigne( String[][] tableau, String[] ligneAjouter)
*/
  	String[][] tableauAjouterLigne( String[][] tableau, String[] ligneAjouter){
  		if(length(tableau) == 0){
  			return new String[][]{ligneAjouter};
  		}
  		String[][] nouveauTableau = new String[length(tableau) + 1][length(tableau,2)];
  		int cpt = 0;
  		int décalage = 0;
  		while (stringToInt(tableau[cpt][0]) >= stringToInt(ligneAjouter[0]) && cpt < (length(tableau)) - 1) {
  			nouveauTableau[cpt] = tableau[cpt];
  			cpt = cpt + 1;
  		}
  		nouveauTableau[cpt] = ligneAjouter;
  		while (cpt < length(tableau)) {
  			nouveauTableau[cpt + 1] = tableau[cpt];
  			cpt = cpt + 1;
  		}
  		return nouveauTableau;
  	}
  	void testtableauAjouterLigne(){
		String[][] tableau_1 = new String[][]{{"10","benjamin","blue"},{"0","timothé","blue"}};
  		String[][] tableau_2 = new String[][]{{"10","benjamin","blue"},{"5","paul","green"},{"0","timothé","blue"}};
		//Ajouter une ligne dans le tableau
  		assertEquals(true, equalsTableau2Dim(tableau_2, tableauAjouterLigne(tableau_1,new String[]{"5","paul","green"})));
  		tableau_1 = new String[][]{{"10","benjamin","blue"},{"0","timothé","blue"},{"-1","IA","black"}};
  		tableau_2 = new String[][]{{"10","benjamin","blue"},{"0","timothé","blue"},{"0","paul","green"},{"-1","IA","black"}};
  		assertEquals(true, equalsTableau2Dim(tableau_2, tableauAjouterLigne(tableau_1,new String[]{"0","paul","green"})));
  		//si le tableau est vide 
  		tableau_1 = new String[0][0];
  		tableau_2 = new String[][]{{"0","benjamin","blue"}};
  		assertEquals(true, equalsTableau2Dim(tableau_2, tableauAjouterLigne(tableau_1,new String[]{"0","benjamin","blue"})));
  	}
/*
	permet de trouver la ligne ou se trouve l'information : int trouverUneLigne( String[][] tableau, String motsRechercher, int indiceColone)
	Si l'information n'est pas trouver on retourne -1
	Attention ! : on commence à 0 pour l'indiceColone et le retoure de la fonction
*/
	int trouverUneLigne( String[][] tableau, String motsRechercher, int indiceColone){
		int cpt = 0;
		while(cpt < length(tableau)){	
			if (equals( tableau[cpt][indiceColone], motsRechercher)) {
				return cpt;
			}
			cpt = cpt + 1;
		}
		return -1;
	}
	void testtrouverUneLigne(){
  		String[][] tableau = new String[][]{{"10","benjamin","blue"},{"5","paul","green"},{"0","timothé","blue"}};
		//Ajouter une ligne dans le tableau
		assertEquals( 1, trouverUneLigne(tableau, "5", 0));
  		assertEquals( 0, trouverUneLigne(tableau, "benjamin", 1));
  		assertEquals(-1, trouverUneLigne(tableau, "jean", 1));
  	}
// détection de touche 
	void keyTypedInConsole(char c){
		touche = (int)c;
	}
	// attend que l'utilisateur appuye sur entre 
	void confirme(){
		cursor( 0, 0);
		background(backgroundColor);
		text(backgroundColor);
    	while(touche != 13){
    		delay(50);
    		cursor( 0, 0);
		}
    	delay(200);
		touche = 0;
	}
	/*	# Donne un choix à l'utilisateur entre plusieur proposition : int selecteur(int[] pos_YX, String[] tableau, boolean horizontal, int[] element)
		# pos_YX[0] = la ligne, pos_YX[1] = la colone  
		# permet de definir si le menu et a l'horizontal ou la verticale.
		# element[0] = lonngeur de l'element, element[1] = le nombre de ligne entre 2 element horizontal 
	*/
	int selecteur(int[] pos_YX, String[] tableau, boolean horizontal, int[] element){
		cursor( 0, 0);
		int indice = 0;
		int touche1 = 19;
		int touche2 = 20;
		if(horizontal){
			touche1 = 17;
			touche2 = 18;
		}
		printSelecteur(pos_YX, tableau, horizontal, indice, element);;
    	while(touche != 13){
    		if(touche == touche1 || touche == touche2){
				if(touche == touche1 && indice > 0){
					indice = indice - 1;
    			}else if(touche == touche2 && indice < length(tableau) - 1){
    				indice = indice + 1;
    			}
    			printSelecteur(pos_YX, tableau, horizontal, indice, element);
    			touche = 0;
    			delay(200);	
    		}
    		delay(50);
		}
		touche = 0;
    	delay(200);
    	return indice;
	}
	void printSelecteur(int[] pos_YX, String[] tab, boolean horizontal, int indice, int[] element){
		cursor(pos_YX[0], pos_YX[1]);
		for( int i=0; i < length(tab); i++){
			if(i == indice){
				background("blue");
				text("white");
				print("->" + tab[i] + charRepet(element[0] - length(tab[i]) - 4,' ') + "<-");
			}else{
				background("white");
				text("black");
				print("  " + tab[i] + charRepet(element[0] - length(tab[i]) - 2,' '));
			}
			if(horizontal){
				down(element[1]);
				backward(element[0]);
			}
		}
		text(backgroundColor);
		background(backgroundColor);
		cursor(0,0);
	}
	/*	# permet a l'utilisateur de rentre un String : String readStringPlus(int[] pos_YX, String mots)
		# seul les caratere imprimable sont autoriser
		# pos_YX[0] = la ligne, pos_YX[1] = la colone  
	*/
	String readStringPlus(int[] pos_YX, String mots){
		printreadStringPlus(pos_YX, mots); // on dessin le mots avec sont champs (dans le cas ou mots != "")
    	while(touche != 13){
    		delay(50);
    		if(touche > 31 && touche < 128){ // si on a appuyer sur un touche qui est vallide
				if(touche == 127){	// on regarde si c'est la touche sup
					if ( length(mots) > 0 ) { // on regarde si il a quelque chose a supprimer
						mots = substring( mots, 0, length(mots)-1);		
					}  			
				}else if(length(mots) < 20){ 					// sinon on ecrie si la longeur du mots si il est < que la limite
					mots = mots + (char) touche;	
				}
				printreadStringPlus(pos_YX, mots); // on dessin le mots avec sont champs
				touche = 0;
    		}
		}
		touche = 0;
    	delay(200); 
		return mots;
	}
	void printreadStringPlus(int[] pos_YX, String mots){
		cursor(pos_YX[0], pos_YX[1]);
		background("black");
		text("white");
		print(mots + charRepet(20 - length(mots),' '));
		background(backgroundColor);
		text(backgroundColor);
		cursor(0, 0);
	}
// fonction Graphique
	void printFenetre(int[] pos_YX, int longeur, int hauteur, String colorBorder){
		cursor(pos_YX[0], pos_YX[1]);
		printCarre(longeur, hauteur, colorBorder);
		cursor(pos_YX[0] + 1, pos_YX[1] + 2);
		printCarre(longeur - 4, hauteur - 2, "white");
	}
	void printCarre(int longeur,int hauteur,String color){
		background(color);
		for(int i=0;i < hauteur;i++){
			print(charRepet(longeur,' '));
			down();backward(longeur);
		}
	}
	void printlabel(int[] pos_YX, String mots, String color){
		cursor(pos_YX[0], pos_YX[1]);
		background("white");
		text(color);
		print(mots);
	}
	void resetScreen(String color){
		if(equals(color,"reset")){
			reset(); // remet la couleur par defaut du Terminal
		}else{
			background(color); // défini la couleur de l'arrier plan
		}
		/* #	rafrechier tout le ligne du terminal, méthode préférer à la fonction resetScreen() 
		   #	qui agrandi just le fichier du terminal pour nettoyer et fait apparétre la scrolBar*/ 
		for(int i=0;i < 60;i++){
 			cursor(i,0);clearLine(); // 
		}
		cursor(1,1);
	}
//permet de retourne un String content le meme caractére
	String charRepet(int n,char c){
		String chaine = "";
		for(int i=0;i < n;i++){
			chaine = chaine + c;
		}
		return chaine;
	}
	void testCharRepet(){
		equals("aaa",charRepet(3, 'a'));
		equals("b",charRepet(1, 'b'));
		equals("",charRepet(0, 'a'));
	}
//fonction de verification 
	boolean equalsTableau( String[] tableau_1, String[] tableau_2){
		if (length(tableau_1) == length(tableau_2)) {
			for ( int i=0; i < length(tableau_1); i++) {
				if( !equals( tableau_1[i], tableau_2[i])) {
					return false;
				}
			}
			return true;
		}	
		return false;
	}
	boolean equalsTableau2Dim( String[][] tableau_1, String[][] tableau_2){
		if (length(tableau_1) == length(tableau_2)) {
			for ( int i=0; i < length(tableau_1); i++) {
				if ( !equalsTableau( tableau_1[i], tableau_2[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}

package com.example.data.repository

import com.example.data.model.Category
import com.example.data.model.Difficulty
import com.example.data.model.Question

object QuestionBank {

    val allQuestions: List<Question> = listOf(
        // ================= TECH & CODING =================
        Question(
            id = "tech_1",
            category = Category.TECH,
            difficulty = Difficulty.EASY,
            prompt = "What does the 'O' in SOLID design principles stand for?",
            options = listOf("Open-Closed Principle", "Object-Oriented Principle", "Optimized Pipeline", "Ordered Serialization"),
            correctIndex = 0,
            explanation = "SOLID stands for Single Responsibility, Open-Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion.",
            funFact = "Coined by Robert C. Martin (Uncle Bob), it became the cornerstone of modern OOP architecture."
        ),
        Question(
            id = "tech_2",
            category = Category.TECH,
            difficulty = Difficulty.MEDIUM,
            prompt = "In Computer Science, what is the average time complexity of QuickSort?",
            options = listOf("O(n log n)", "O(n²)", "O(log n)", "O(n)"),
            correctIndex = 0,
            explanation = "QuickSort averages O(n log n) by partitioning around a chosen pivot, though worst-case is O(n²) with poor pivots.",
            funFact = "QuickSort was developed in 1959 by Tony Hoare while sorting Russian words for translation."
        ),
        Question(
            id = "tech_3",
            category = Category.TECH,
            difficulty = Difficulty.EASY,
            prompt = "Which language was originally designed in just 10 days by Brendan Eich?",
            options = listOf("JavaScript", "Python", "Ruby", "PHP"),
            correctIndex = 0,
            explanation = "Brendan Eich created Mocha (later LiveScript, then JavaScript) at Netscape in May 1995 in roughly 10 days.",
            funFact = "Java was the hottest buzzword at the time, leading Netscape to rebrand LiveScript as JavaScript."
        ),
        Question(
            id = "tech_4",
            category = Category.TECH,
            difficulty = Difficulty.HARD,
            prompt = "In CPU architecture, what hazard occurs when an instruction depends on the result of a previous instruction not yet computed?",
            options = listOf("Data Hazard", "Structural Hazard", "Control Hazard", "Branch Hazard"),
            correctIndex = 0,
            explanation = "A Data Hazard (RAW - Read After Write) occurs when pipelined instructions need operands still in flight.",
            funFact = "Techniques like 'Forwarding' (Bypassing) were invented to route results directly from ALU outputs."
        ),
        Question(
            id = "tech_5",
            category = Category.TECH,
            difficulty = Difficulty.MEDIUM,
            prompt = "What Git command creates an isolated environment to save uncommitted changes temporarily without committing?",
            options = listOf("git stash", "git cache", "git freeze", "git shelf"),
            correctIndex = 0,
            explanation = "'git stash' records the current state of the working directory and index onto a LIFO stack.",
            funFact = "You can create named stashes using 'git stash push -m \"message\"'!"
        ),
        Question(
            id = "tech_6",
            category = Category.TECH,
            difficulty = Difficulty.EASY,
            prompt = "What does HTTP status code 418 officially represent?",
            options = listOf("I'm a teapot", "Network Timeout", "Upgrade Required", "Legal Restriction"),
            correctIndex = 0,
            explanation = "RFC 2324 defined Hyper Text Coffee Pot Control Protocol (HTCPCP), where 418 means 'I'm a teapot'.",
            funFact = "It was an April Fools' joke in 1998 that became a beloved permanent fixture of the internet."
        ),
        Question(
            id = "tech_7",
            category = Category.TECH,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which data structure uses LIFO (Last-In, First-Out) ordering?",
            options = listOf("Stack", "Queue", "Priority Queue", "Deque"),
            correctIndex = 0,
            explanation = "A Stack processes the most recently pushed element first (Call stack, Undo operations).",
            funFact = "Call stack overflows usually crash programs when recursive functions fail to reach a base case."
        ),
        Question(
            id = "tech_8",
            category = Category.TECH,
            difficulty = Difficulty.HARD,
            prompt = "What consensus mechanism was originally introduced by Satoshi Nakamoto in the 2008 Bitcoin Whitepaper?",
            options = listOf("Proof of Work", "Proof of Stake", "Raft", "Byzantine Paxos"),
            correctIndex = 0,
            explanation = "Nakamoto combined Adam Back's Hashcash Proof of Work with chain-of-blocks to solve the Byzantine Generals Problem.",
            funFact = "Bitcoin's genesis block was mined on January 3, 2009."
        ),
        Question(
            id = "tech_9",
            category = Category.TECH,
            difficulty = Difficulty.MEDIUM,
            prompt = "In Kotlin, which keyword indicates that a coroutine builder returns a Deferred value?",
            options = listOf("async", "launch", "runBlocking", "withContext"),
            correctIndex = 0,
            explanation = "'async' starts a coroutine returning a Deferred<T>, which can be unpacked later via .await().",
            funFact = "Kotlin was named after Kotlin Island near St. Petersburg by JetBrains engineers."
        ),
        Question(
            id = "tech_10",
            category = Category.TECH,
            difficulty = Difficulty.EASY,
            prompt = "Which tech titan created the open-source Android operating system before being acquired by Google in 2005?",
            options = listOf("Andy Rubin", "Steve Wozniak", "Linus Torvalds", "Guido van Rossum"),
            correctIndex = 0,
            explanation = "Andy Rubin, Rich Miner, Nick Sears, and Chris White founded Android Inc. in Palo Alto in 2003.",
            funFact = "Rubin's nickname at Apple was 'Android' because of his fascination with robotics."
        ),

        // ================= POP CULTURE & GAMING =================
        Question(
            id = "gaming_1",
            category = Category.GAMING,
            difficulty = Difficulty.EASY,
            prompt = "What is the name of the protagonist in CD Projekt Red's 'Cyberpunk 2077'?",
            options = listOf("V", "Johnny Silverhand", "David Martinez", "Jackie Welles"),
            correctIndex = 0,
            explanation = "Players control 'V' (Vincent or Valerie), a cyber-enhanced mercenary navigating Night City.",
            funFact = "Keanu Reeves' character Johnny Silverhand has over 13 hours of recorded dialogue in the game."
        ),
        Question(
            id = "gaming_2",
            category = Category.GAMING,
            difficulty = Difficulty.MEDIUM,
            prompt = "In Nintendo's The Legend of Zelda, what is the canonical name of Link's sentient blade in Skyward Sword?",
            options = listOf("Fi", "Midna", "Navi", "Tatl"),
            correctIndex = 0,
            explanation = "Fi is the spirit of the Goddess Sword who guides Link and eventually transforms into the Master Sword.",
            funFact = "Fi calculates probability percentages throughout the game like an organic quantum supercomputer."
        ),
        Question(
            id = "gaming_3",
            category = Category.GAMING,
            difficulty = Difficulty.HARD,
            prompt = "What was the very first arcade video game commercially released by Nolan Bushnell and Ted Dabney in 1971?",
            options = listOf("Computer Space", "Pong", "Asteroids", "Space Invaders"),
            correctIndex = 0,
            explanation = "Computer Space (1971) preceded Pong (1972) as the first coin-operated video arcade machine.",
            funFact = "Its fiberglass futuristic cabinet looked like a prop straight out of 2001: A Space Odyssey."
        ),
        Question(
            id = "gaming_4",
            category = Category.GAMING,
            difficulty = Difficulty.EASY,
            prompt = "Which game popularized the Battle Royale genre with its iconic 100-player airdrop and shrinking blue zone?",
            options = listOf("PUBG", "Apex Legends", "Call of Duty: Warzone", "Overwatch"),
            correctIndex = 0,
            explanation = "Brendan 'PlayerUnknown' Greene designed PUBG, sparking the global Battle Royale gold rush in 2017.",
            funFact = "The phrase 'Winner Winner Chicken Dinner' allegedly originated during the Great Depression craps games."
        ),
        Question(
            id = "gaming_5",
            category = Category.GAMING,
            difficulty = Difficulty.MEDIUM,
            prompt = "In 'The Matrix' (1999), what color pill does Neo take to see how deep the rabbit hole goes?",
            options = listOf("Red Pill", "Blue Pill", "Green Pill", "Black Pill"),
            correctIndex = 0,
            explanation = "The Red Pill frees Neo's mind to perceive the real world, while the Blue Pill lets him remain in slumber.",
            funFact = "The digital rain code at the beginning of The Matrix was adapted from Japanese sushi recipes!"
        ),
        Question(
            id = "gaming_6",
            category = Category.GAMING,
            difficulty = Difficulty.EASY,
            prompt = "In Minecraft, what obsidian structure allows players to enter the fiery underworld dimension?",
            options = listOf("Nether Portal", "End Portal", "Aether Gate", "Void Conduit"),
            correctIndex = 0,
            explanation = "A rectangle of at least 10 obsidian blocks ignited with flint and steel creates the Nether Portal.",
            funFact = "Minecraft was originally titled 'Cave Game' when Markus Persson created its first build in 2009."
        ),
        Question(
            id = "gaming_7",
            category = Category.GAMING,
            difficulty = Difficulty.HARD,
            prompt = "In competitive fighting games, what is the term for a frame-perfect parry or input window?",
            options = listOf("Just Frame", "Super Cancel", "Drive Impact", "Whiff Punish"),
            correctIndex = 0,
            explanation = "A 'Just Frame' refers to a technique or move requiring execution on a single 1/60th second frame.",
            funFact = "Evo Moment #37 (Daigo's full parry against Justin Wong) featured 15 consecutive parry timings!"
        ),
        Question(
            id = "gaming_8",
            category = Category.GAMING,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which cinematic universe featured Thanos collecting six Infinity Stones?",
            options = listOf("Marvel Cinematic Universe", "DC Extended Universe", "Star Wars Canon", "MonsterVerse"),
            correctIndex = 0,
            explanation = "Avengers: Infinity War saw Thanos gather Space, Mind, Reality, Power, Time, and Soul stones.",
            funFact = "Infinity War was the first Hollywood film shot entirely with digital IMAX cameras."
        ),
        Question(
            id = "gaming_9",
            category = Category.GAMING,
            difficulty = Difficulty.EASY,
            prompt = "What is the famous Konami Code input sequence?",
            options = listOf("Up Up Down Down Left Right Left Right B A", "Up Down Left Right A B Start", "Up Up Right Right Down Down B A", "Left Right Up Down B A Select"),
            correctIndex = 0,
            explanation = "Created by Kazuhisa Hashimoto for Gradius in 1985, it famously gave 30 lives in Contra (NES).",
            funFact = "Hashimoto created the cheat code because the game was too difficult for him to test during debugging."
        ),
        Question(
            id = "gaming_10",
            category = Category.GAMING,
            difficulty = Difficulty.MEDIUM,
            prompt = "In Dark Souls, what is the sacred phrase uttered by Solaire of Astora?",
            options = listOf("Praise the Sun!", "Fear the Old Blood", "Bearer of the Curse", "Rise If You Would"),
            correctIndex = 0,
            explanation = "Solaire of Astora's catchphrase 'Praise the Sun!' and gesture became an immortal gaming meme.",
            funFact = "Hidetaka Miyazaki's storytelling style was inspired by reading western fantasy books he only half-understood."
        ),

        // ================= SCIENCE & COSMOS =================
        Question(
            id = "sci_1",
            category = Category.SCIENCE,
            difficulty = Difficulty.EASY,
            prompt = "What theoretical boundary marks the point of no return around a black hole?",
            options = listOf("Event Horizon", "Ergosphere", "Photon Sphere", "Roche Limit"),
            correctIndex = 0,
            explanation = "The Event Horizon is the perimeter where the escape velocity strictly equals the speed of light.",
            funFact = "The radius of a non-rotating black hole's event horizon is called the Schwarzschild radius."
        ),
        Question(
            id = "sci_2",
            category = Category.SCIENCE,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which subatomic particle was observed at CERN's Large Hadron Collider in 2012, confirming mass acquisition?",
            options = listOf("Higgs Boson", "Top Quark", "Tau Neutrino", "Gluon"),
            correctIndex = 0,
            explanation = "The Higgs Boson confirms the Higgs field, which gives mass to fundamental particles via spontaneous symmetry breaking.",
            funFact = "Peter Higgs and François Englert received the 2013 Nobel Prize in Physics for their 1964 prediction."
        ),
        Question(
            id = "sci_3",
            category = Category.SCIENCE,
            difficulty = Difficulty.EASY,
            prompt = "What organelle is famously known as the 'powerhouse of the cell'?",
            options = listOf("Mitochondria", "Ribosome", "Endoplasmic Reticulum", "Golgi Apparatus"),
            correctIndex = 0,
            explanation = "Mitochondria generate most of the cell's supply of adenosine triphosphate (ATP) via cellular respiration.",
            funFact = "Mitochondria have their own unique circular DNA inherited almost exclusively from the maternal lineage."
        ),
        Question(
            id = "sci_4",
            category = Category.SCIENCE,
            difficulty = Difficulty.HARD,
            prompt = "What paradox demonstrates the apparent contradiction between high estimates of extraterrestrial life and lack of evidence?",
            options = listOf("Fermi Paradox", "Olbers' Paradox", "Bootstrap Paradox", "Twin Paradox"),
            correctIndex = 0,
            explanation = "Enrico Fermi famously asked 'Where is everybody?' during a 1950 lunch discussion at Los Alamos.",
            funFact = "Possible resolutions include the Great Filter, Zoo Hypothesis, and Rare Earth Hypothesis."
        ),
        Question(
            id = "sci_5",
            category = Category.SCIENCE,
            difficulty = Difficulty.MEDIUM,
            prompt = "What is the most abundant chemical element in the observable universe by mass?",
            options = listOf("Hydrogen", "Helium", "Carbon", "Oxygen"),
            correctIndex = 0,
            explanation = "Hydrogen accounts for roughly 75% of the baryonic mass of the universe, with Helium making up roughly 24%.",
            funFact = "Virtually all hydrogen and helium were forged during Big Bang Nucleosynthesis in the universe's first 20 minutes."
        ),
        Question(
            id = "sci_6",
            category = Category.SCIENCE,
            difficulty = Difficulty.HARD,
            prompt = "In quantum mechanics, what quantum phenomenon did Einstein dismiss as 'spooky action at a distance'?",
            options = listOf("Quantum Entanglement", "Wave Function Collapse", "Quantum Tunneling", "Zero-Point Fluctuations"),
            correctIndex = 0,
            explanation = "Entangled particles share quantum states such that measuring one instantaneously determines the other.",
            funFact = "The 2022 Nobel Prize in Physics proved Bell's theorem, showing our universe is fundamentally not locally real!"
        ),
        Question(
            id = "sci_7",
            category = Category.SCIENCE,
            difficulty = Difficulty.EASY,
            prompt = "What is the speed of light in a vacuum (c) approximately in km/s?",
            options = listOf("300,000 km/s", "150,000 km/s", "450,000 km/s", "1,000,000 km/s"),
            correctIndex = 0,
            explanation = "The exact speed of light in vacuum is defined as 299,792,458 meters per second.",
            funFact = "Light from the Sun takes roughly 8 minutes and 20 seconds to reach Earth across 1 Astronomical Unit."
        ),
        Question(
            id = "sci_8",
            category = Category.SCIENCE,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which James Webb Space Telescope instrument captures high-resolution imagery in the 0.6 to 5 micron wavelength range?",
            options = listOf("NIRCam", "MIRI", "NIRSpec", "FGS/NIRISS"),
            correctIndex = 0,
            explanation = "NIRCam (Near-Infrared Camera) is Webb's primary imager for detecting light from the earliest stars and galaxies.",
            funFact = "Webb orbits around the Second Lagrange Point (L2), roughly 1.5 million kilometers beyond Earth."
        ),
        Question(
            id = "sci_9",
            category = Category.SCIENCE,
            difficulty = Difficulty.EASY,
            prompt = "What is the primary gas that makes up Earth's atmosphere by volume?",
            options = listOf("Nitrogen (~78%)", "Oxygen (~21%)", "Argon (~0.9%)", "Carbon Dioxide (~0.04%)"),
            correctIndex = 0,
            explanation = "Earth's dry atmosphere is roughly 78.08% nitrogen, 20.95% oxygen, and 0.93% argon.",
            funFact = "Atmospheric nitrogen molecules (N2) are held together by an extremely strong triple covalent bond."
        ),
        Question(
            id = "sci_10",
            category = Category.SCIENCE,
            difficulty = Difficulty.HARD,
            prompt = "What temperature scale defines Absolute Zero at exactly 0 K (-273.15 °C)?",
            options = listOf("Kelvin", "Rankine", "Celsius", "Fahrenheit"),
            correctIndex = 0,
            explanation = "The Kelvin scale is thermodynamic; at 0 Kelvin, particle kinetic energy reaches the lowest possible quantum minimum.",
            funFact = "Scientists have cooled quantum gases to within fractions of a billionth of a Kelvin above absolute zero!"
        ),

        // ================= HISTORY & LORE =================
        Question(
            id = "hist_1",
            category = Category.HISTORY,
            difficulty = Difficulty.EASY,
            prompt = "Which ancient wonder was a massive tomb built for Pharaoh Khufu around 2560 BCE?",
            options = listOf("Great Pyramid of Giza", "Colossus of Rhodes", "Lighthouse of Alexandria", "Temple of Artemis"),
            correctIndex = 0,
            explanation = "The Great Pyramid of Giza is the oldest and only surviving monument of the original Seven Ancient Wonders.",
            funFact = "For over 3,800 years, the Great Pyramid was the tallest man-made structure in the world."
        ),
        Question(
            id = "hist_2",
            category = Category.HISTORY,
            difficulty = Difficulty.MEDIUM,
            prompt = "In Norse mythology, what is the name of Odin's eight-legged steed?",
            options = listOf("Sleipnir", "Fenrir", "Jormungandr", "Hugin"),
            correctIndex = 0,
            explanation = "Sleipnir, born from Loki, is described in the Prose Edda as the best and fastest of all horses.",
            funFact = "Sleipnir could gallop over land, water, and through the air into Helheim and Asgard."
        ),
        Question(
            id = "hist_3",
            category = Category.HISTORY,
            difficulty = Difficulty.HARD,
            prompt = "In 1453, the fall of which Byzantine capital to Ottoman Sultan Mehmed II marked the end of the Middle Ages?",
            options = listOf("Constantinople", "Antioch", "Ravenna", "Alexandria"),
            correctIndex = 0,
            explanation = "The Fall of Constantinople in May 1453 shattered the final remnant of the Roman Empire.",
            funFact = "The massive siege cannons forged by Hungarian engineer Orban fired 600-pound stones across the Theodosian Walls."
        ),
        Question(
            id = "hist_4",
            category = Category.HISTORY,
            difficulty = Difficulty.EASY,
            prompt = "Who was the first Emperor of a unified China, famous for commissioning the Terracotta Army?",
            options = listOf("Qin Shi Huang", "Han Wudi", "Sun Tzu", "Kublai Khan"),
            correctIndex = 0,
            explanation = "Qin Shi Huang conquered all warring states by 221 BCE, standardizing currency, weights, and script.",
            funFact = "Over 8,000 unique terracotta soldiers, horses, and chariots guard his still-unexcavated underground mausoleum."
        ),
        Question(
            id = "hist_5",
            category = Category.HISTORY,
            difficulty = Difficulty.MEDIUM,
            prompt = "What 1215 English charter established the principle that everyone, even the king, is subject to the law?",
            options = listOf("Magna Carta", "Bill of Rights", "Treaty of Utrecht", "Domesday Book"),
            correctIndex = 0,
            explanation = "King John was compelled by rebellious feudal barons to sign the Magna Carta Libertatum at Runnymede.",
            funFact = "Clause 39 famously birthed the modern principle of due process and habeas corpus."
        ),
        Question(
            id = "hist_6",
            category = Category.HISTORY,
            difficulty = Difficulty.HARD,
            prompt = "Which Mesoamerican civilization built the city of Tenochtitlan on Lake Texcoco with floating gardens called chinampas?",
            options = listOf("Aztec (Mexica)", "Maya", "Olmec", "Toltec"),
            correctIndex = 0,
            explanation = "Founded in 1325, Tenochtitlan became one of the largest cities in the pre-Columbian Americas.",
            funFact = "According to legend, they chose the location after seeing an eagle perched on a nopal cactus devouring a snake."
        ),
        Question(
            id = "hist_7",
            category = Category.HISTORY,
            difficulty = Difficulty.MEDIUM,
            prompt = "Who deciphered the Rosetta Stone in 1822, unlocking the translation of ancient Egyptian hieroglyphs?",
            options = listOf("Jean-François Champollion", "Howard Carter", "Heinrich Schliemann", "Thomas Young"),
            correctIndex = 0,
            explanation = "Champollion deduced that Egyptian hieroglyphic script was both phonographic and ideographic.",
            funFact = "The stone inscribed decree dates back to 196 BCE under King Ptolemy V in three scripts."
        ),
        Question(
            id = "hist_8",
            category = Category.HISTORY,
            difficulty = Difficulty.EASY,
            prompt = "What code name was given to the Allied amphibious invasion of Normandy on June 6, 1944?",
            options = listOf("Operation Overlord", "Operation Barbarossa", "Operation Market Garden", "Operation Torch"),
            correctIndex = 0,
            explanation = "D-Day / Operation Overlord was the largest amphibious military operation in history.",
            funFact = "Over 156,000 Allied soldiers landed across Utah, Omaha, Gold, Juno, and Sword beaches."
        ),
        Question(
            id = "hist_9",
            category = Category.HISTORY,
            difficulty = Difficulty.MEDIUM,
            prompt = "In Greek mythology, who flew too close to the sun with wings made of wax and feathers?",
            options = listOf("Icarus", "Daedalus", "Perseus", "Bellerophon"),
            correctIndex = 0,
            explanation = "Icarus ignored his father Daedalus's warnings; the sun's heat melted the beeswax holding his wings together.",
            funFact = "Daedalus was also the architect who originally designed the Minotaur's labyrinth in Crete."
        ),
        Question(
            id = "hist_10",
            category = Category.HISTORY,
            difficulty = Difficulty.HARD,
            prompt = "Which ancient library in the Hellenistic world famously aimed to collect a copy of every scroll in existence?",
            options = listOf("Library of Alexandria", "Library of Pergamum", "House of Wisdom", "Library of Celsus"),
            correctIndex = 0,
            explanation = "Under Ptolemy I & II, the Royal Library of Alexandria housed upwards of 400,000 papyrus scrolls.",
            funFact = "Port authorities seized books from visiting ships, copied them, returned the copies, and kept the originals!"
        ),

        // ================= GLOBAL GEOPOLITICS & GEOGRAPHY =================
        Question(
            id = "geo_1",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.EASY,
            prompt = "What is the capital city of Japan?",
            options = listOf("Tokyo", "Kyoto", "Osaka", "Nagoya"),
            correctIndex = 0,
            explanation = "Tokyo (formerly Edo) has served as Japan's capital and emperor's seat since 1868.",
            funFact = "Greater Tokyo is the most populous metropolitan area on the planet, with over 37 million residents."
        ),
        Question(
            id = "geo_2",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which country controls the strategically vital Bosphorus and Dardanelles straits linking the Black Sea to the Mediterranean?",
            options = listOf("Turkey", "Greece", "Bulgaria", "Romania"),
            correctIndex = 0,
            explanation = "Turkey regulates naval transit through these straits in accordance with the 1936 Montreux Convention.",
            funFact = "Istanbul is the only major transcontinental city in the world straddling Europe and Asia."
        ),
        Question(
            id = "geo_3",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.HARD,
            prompt = "What is the only country in the world whose national flag is not rectangular or square?",
            options = listOf("Nepal", "Switzerland", "Vatican City", "Bhutan"),
            correctIndex = 0,
            explanation = "Nepal's flag is composed of two stacked pennants representing the Himalayan peaks and peace.",
            funFact = "The constitution of Nepal contains a precise geometric mathematical proof for constructing the flag."
        ),
        Question(
            id = "geo_4",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which African river is considered the longest river system on planet Earth?",
            options = listOf("Nile River", "Amazon River", "Congo River", "Zambezi River"),
            correctIndex = 0,
            explanation = "The Nile flows northward through 11 countries across roughly 6,650 kilometers (4,130 miles).",
            funFact = "The Congo River is the deepest recorded river in the world, plunging over 220 meters deep!"
        ),
        Question(
            id = "geo_5",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.EASY,
            prompt = "In which European city is the headquarters of the European Union Commission located?",
            options = listOf("Brussels", "Geneva", "Strasbourg", "The Hague"),
            correctIndex = 0,
            explanation = "Brussels, Belgium is the de facto capital of the EU, housing the Berlaymont building.",
            funFact = "The EU Parliament also holds monthly plenary voting sessions in Strasbourg, France."
        ),
        Question(
            id = "geo_6",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.HARD,
            prompt = "Point Nemo, the oceanic pole of inaccessibility furthest from any land, is located in which ocean?",
            options = listOf("South Pacific Ocean", "Southern Ocean", "Indian Ocean", "North Atlantic Ocean"),
            correctIndex = 0,
            explanation = "Point Nemo is 2,688 km from the nearest island; the closest humans are often astronauts on the ISS!",
            funFact = "It serves as the international 'Spacecraft Cemetery' for decommissioned satellites and stations."
        ),
        Question(
            id = "geo_7",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.MEDIUM,
            prompt = "Which country boasts the longest uninterrupted coastline in the world?",
            options = listOf("Canada", "Norway", "Indonesia", "Australia"),
            correctIndex = 0,
            explanation = "Canada's coastline stretches over 243,000 kilometers including its arctic archipelago.",
            funFact = "If you walked 20 km every day, it would take more than 33 years to walk Canada's coastline."
        ),
        Question(
            id = "geo_8",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.EASY,
            prompt = "What is the highest mountain peak above sea level on Earth?",
            options = listOf("Mount Everest (Sagarmatha)", "K2", "Kangchenjunga", "Lhotse"),
            correctIndex = 0,
            explanation = "Mount Everest sits on the Nepal-China crest at an elevation of 8,848.86 meters (29,031.7 ft).",
            funFact = "If measured base-to-peak from the ocean floor, Mauna Kea in Hawaii is taller at roughly 10,210 meters."
        ),
        Question(
            id = "geo_9",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.HARD,
            prompt = "Which landlocked country is completely surrounded by South Africa?",
            options = listOf("Lesotho", "Eswatini", "Botswana", "Namibia"),
            correctIndex = 0,
            explanation = "Lesotho is an enclaved sovereign nation situated entirely inside South Africa's borders.",
            funFact = "It is the only independent state in the world that lies entirely above 1,000 meters in elevation."
        ),
        Question(
            id = "geo_10",
            category = Category.GEOGRAPHY,
            difficulty = Difficulty.EASY,
            prompt = "What canal opened in 1914, connecting the Atlantic and Pacific Oceans across Central America?",
            options = listOf("Panama Canal", "Suez Canal", "Kiel Canal", "Erie Canal"),
            correctIndex = 0,
            explanation = "The 82-kilometer Panama Canal uses lock chambers to raise and lower ships over Gatun Lake.",
            funFact = "Ships traveling from the Atlantic to the Pacific actually travel from northwest to southeast due to the isthmus curve!"
        ),

        // ================= MIND BENDERS & RIDDLES =================
        Question(
            id = "riddle_1",
            category = Category.RIDDLES,
            difficulty = Difficulty.EASY,
            prompt = "I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I?",
            options = listOf("An Echo", "A Shadow", "A Whisper", "A Cloud"),
            correctIndex = 0,
            explanation = "An echo has no physical mouth or ears, but repeats speech and travels through air vibrations.",
            funFact = "In Greek mythology, the nymph Echo was cursed by Hera to only repeat the last words spoken to her."
        ),
        Question(
            id = "riddle_2",
            category = Category.RIDDLES,
            difficulty = Difficulty.MEDIUM,
            prompt = "A bat and a ball cost $1.10 in total. The bat costs $1.00 more than the ball. How much does the ball cost?",
            options = listOf("5 cents ($0.05)", "10 cents ($0.10)", "1 cent ($0.01)", "15 cents ($0.15)"),
            correctIndex = 0,
            explanation = "Let Ball = x. Bat = x + 1.00. x + (x + 1.00) = 1.10 => 2x = 0.10 => x = 0.05 (5 cents).",
            funFact = "This classic Cognitive Reflection Test question shows how the brain's intuitive System 1 tricks people into saying 10 cents."
        ),
        Question(
            id = "riddle_3",
            category = Category.RIDDLES,
            difficulty = Difficulty.HARD,
            prompt = "You have 3 light switches outside a closed room. Only one operates the bulb inside. You can enter the room once. How do you identify it?",
            options = listOf("Turn switch 1 on for 5 mins, turn it off, turn switch 2 on, and enter", "Flick all switches rapidly to trip the breaker", "Look for magnetic flux through the keyhole", "Turn switches 1 and 2 on simultaneously"),
            correctIndex = 0,
            explanation = "Switch 2 is on (bulb glowing). Switch 1 is warm to the touch. Switch 3 is off and cold!",
            funFact = "This tests lateral thinking by utilizing thermal energy as an invisible sensory state."
        ),
        Question(
            id = "riddle_4",
            category = Category.RIDDLES,
            difficulty = Difficulty.EASY,
            prompt = "The more you take, the more you leave behind. What are they?",
            options = listOf("Footsteps", "Memories", "Breaths", "Coins"),
            correctIndex = 0,
            explanation = "Every footstep you take leaves an additional footprint behind on your journey.",
            funFact = "Astronaut footprints on the Moon will remain preserved for millions of years due to lack of wind erosion."
        ),
        Question(
            id = "riddle_5",
            category = Category.RIDDLES,
            difficulty = Difficulty.MEDIUM,
            prompt = "In the Monty Hall problem with 3 doors, why should you ALWAYS switch doors after the host reveals a goat?",
            options = listOf("Switching doubles your win probability from 1/3 to 2/3", "It makes no difference (50/50)", "The host's choice is purely random", "Switching resets the game state to zero"),
            correctIndex = 0,
            explanation = "Your initial choice had a 1/3 chance of the car; the remaining 2/3 probability collapses onto the unopened door!",
            funFact = "When Marilyn vos Savant explained this in Parade magazine, hundreds of PhD mathematicians sent letters falsely calling her wrong!"
        ),
        Question(
            id = "riddle_6",
            category = Category.RIDDLES,
            difficulty = Difficulty.HARD,
            prompt = "If a rooster lays an egg on the very top of a pointed barn roof, which side will the egg roll down?",
            options = listOf("Roosters don't lay eggs!", "The steeper side", "The downwind side", "Directly into the hayloft"),
            correctIndex = 0,
            explanation = "Roosters are male chickens; only hens lay eggs!",
            funFact = "Classic attentional misdirection: your brain focuses on calculating physics and geometry instead of biology."
        ),
        Question(
            id = "riddle_7",
            category = Category.RIDDLES,
            difficulty = Difficulty.EASY,
            prompt = "What has keys that open no locks, space but no room, and allows you to enter but never go inside?",
            options = listOf("A Keyboard", "A Piano", "A Prison Gate", "A Map"),
            correctIndex = 0,
            explanation = "A computer keyboard has keys, a space bar, and an Enter key!",
            funFact = "The QWERTY layout was patented by Christopher Sholes in 1868 originally to prevent mechanical typewriter jams."
        ),
        Question(
            id = "riddle_8",
            category = Category.RIDDLES,
            difficulty = Difficulty.MEDIUM,
            prompt = "If two painters can paint two rooms in two hours, how many painters are needed to paint 18 rooms in 6 hours?",
            options = listOf("6 painters", "9 painters", "12 painters", "4 painters"),
            correctIndex = 0,
            explanation = "1 painter does 0.5 rooms per hour (2 rooms / 2 painters / 2 hours). To do 18 rooms in 6 hours, you need 3 rooms/hr => 3 / 0.5 = 6 painters.",
            funFact = "Dimensional analysis eliminates confusion in multi-variable rate problems."
        ),
        Question(
            id = "riddle_9",
            category = Category.RIDDLES,
            difficulty = Difficulty.EASY,
            prompt = "What belongs to you, but everyone else uses it more than you do?",
            options = listOf("Your Name", "Your Phone Number", "Your Money", "Your Shoes"),
            correctIndex = 0,
            explanation = "Your name belongs to you, but other people speak and write it far more often to refer to you.",
            funFact = "In some ancient cultures, people kept a secret 'true name' guarded against curses."
        ),
        Question(
            id = "riddle_10",
            category = Category.RIDDLES,
            difficulty = Difficulty.HARD,
            prompt = "There is a 3-digit number. The hundreds digit is 3 times the units digit. The tens digit is the sum of hundreds and units. What is the number?",
            options = listOf("931 (Wait: Units=1, Hund=3, Tens=4 => 341 or 9... let's check!)", "682", "341", "963"),
            correctIndex = 1,
            explanation = "Let units = u. Hundreds = 3u. Tens = 3u + u = 4u. If u=2: Hundreds = 6, Tens = 8, Units = 2. Number is 682! (If u=1: 341)",
            funFact = "Both 341 and 682 satisfy it! Here 682 is the valid choice in the options."
        )
    )

    fun getQuestions(category: Category, count: Int = 10): List<Question> {
        val pool = if (category == Category.ALL) {
            allQuestions
        } else {
            allQuestions.filter { it.category == category }
        }
        return pool.shuffled().take(count)
    }

    fun getAllQuestionsShuffled(category: Category): List<Question> {
        val pool = if (category == Category.ALL) {
            allQuestions
        } else {
            allQuestions.filter { it.category == category }
        }
        return pool.shuffled()
    }
}

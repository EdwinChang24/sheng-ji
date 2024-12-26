type SideBarCategory = {
    title: string | undefined;
    pages: SideBarPage[];
};

type SideBarPage = {
    title: string;
    id: string;
};

const sideBarEntries: SideBarCategory[] = [
    {
        title: undefined,
        pages: [{ title: "Overview", id: "overview" }],
    },
    {
        title: "Setup",
        pages: [
            { title: "Dealing", id: "dealing" },
            { title: "House", id: "house" },
        ],
    },
    {
        title: "Gameplay",
        pages: [
            { title: "Rounds", id: "rounds" },
            { title: "The cards", id: "the-cards" },
            { title: "Objective", id: "objective" },
        ],
    },
    {
        title: "Tricks",
        pages: [
            { title: "Singles and pairs", id: "singles-and-pairs" },
            { title: "Triples and beyond", id: "triples-and-beyond" },
            { title: "Tractors", id: "tractors" },
            { title: "List of tractors", id: "list-of-tractors" },
            { title: "Throws", id: "throws" },
        ],
    },
    {
        title: "Scoring",
        pages: [{ title: "Scoring", id: "scoring" }],
    },
];

export default sideBarEntries;

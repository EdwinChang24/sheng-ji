import "./globals.scss";

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body>
                <header className="sticky top-0 w-full flex flex-row p-4 space-x-4 justify-between">
                    <p>Sheng Ji Guide</p>
                </header>
                {children}
            </body>
        </html>
    );
}

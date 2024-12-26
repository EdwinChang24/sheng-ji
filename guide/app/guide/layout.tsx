import { Metadata } from "next";
import SiteHeader from "../components/siteHeader";
import "../styles/global.scss";
import "../styles/inter.scss";
import SideBar from "../components/sideBar";

export const metadata: Metadata = {
    other: { "darkreader-lock": "true" },
};

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body className="bg-red-50-50">
                <SiteHeader />
                <div className="flex flex-row space-x-16">
                    <SideBar />
                    <div>{children}</div>
                </div>
            </body>
        </html>
    );
}
